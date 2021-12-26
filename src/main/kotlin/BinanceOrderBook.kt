import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.math.BigDecimal

class BinanceOrderBook : OrderBook() {
    fun initialize(rawJson: String) { try {
        println("Binance: $rawJson")
        initialValue = HistoryEntity(rawJson)

        val initialOrderBook = Json { ignoreUnknownKeys = true }.decodeFromString<InitialBinanceOrderBook>(rawJson)
        ask = mapConvert(initialOrderBook.asks)
        bid = mapConvert(initialOrderBook.bids)
        updatedAt = initialOrderBook.lastUpdateId
        initialized = true

        preinitialize.forEach { update(it.raw) }

    } catch (e: Throwable) { println("Binance initialization failed") ; e.printStackTrace() } }

    fun update(rawJson: String, maximum: Long? = null) { try {
        println("Binance: $rawJson")
        store(rawJson)

        val updatedOrderBook = Json { ignoreUnknownKeys = true }.decodeFromString<UpdatedBinanceOrderBook>(rawJson).data
        if (updatedOrderBook.u <= updatedAt) return
        if (maximum != null && updatedOrderBook.u > maximum) return

        ask = applyUpdates(ask, updatedOrderBook.a)
        bid = applyUpdates(bid, updatedOrderBook.b)
        updatedAt = updatedOrderBook.u
    } catch (e: Throwable) { println("Binance update failed") ; e.printStackTrace() } }

    private fun mapConvert(from: List<List<String>>): Map<BigDecimal, BigDecimal> {
        val toReturn: MutableMap<BigDecimal, BigDecimal> = mutableMapOf()
        from.forEach {
            val k = BigDecimal(it[0]).stripTrailingZeros()
            val v = BigDecimal(it[1]).stripTrailingZeros()
            toReturn[k] = v
        }
        return toReturn
    }

    private fun applyUpdates(base: Map<BigDecimal, BigDecimal>, delta: List<List<String>>): Map<BigDecimal, BigDecimal> {
        val toReturn = base.toMutableMap()
        for ((k,v) in mapConvert(delta)) {
            if (v.stripTrailingZeros() == BigDecimal(0)) { toReturn.remove(k) }
            else { toReturn[k.stripTrailingZeros()] = v.stripTrailingZeros() }
        }
        return toReturn
    }
}

@Serializable
data class InitialBinanceOrderBook(val lastUpdateId: Long, val bids: List<List<String>>, val asks: List<List<String>>)

@Serializable
data class UpdatedBinanceOrderBookData(val e: String, val s: String, val u: Long, val b: List<List<String>>, val a: List<List<String>>)

@Serializable
data class UpdatedBinanceOrderBook(val stream: String, val data: UpdatedBinanceOrderBookData)