import java.math.BigDecimal
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.math.min

abstract class OrderBook {
    val preinitialize: ConcurrentLinkedDeque<HistoryEntity> = ConcurrentLinkedDeque()
    val history: ConcurrentLinkedDeque<HistoryEntity> = ConcurrentLinkedDeque()
    var ask: Map<BigDecimal, BigDecimal> = mapOf()
    var bid: Map<BigDecimal, BigDecimal> = mapOf()
    val askKeys:() -> List<BigDecimal> = { ask.keys.sorted() }
    val bidKeys:() -> List<BigDecimal> = { bid.keys.sorted().reversed() }
    lateinit var initialValue: HistoryEntity
    var updatedAt: Long = 0
    var initialized = false

    fun getL2(depth: Int = 5): List<StackPair> {
        val toReturn: MutableList<StackPair> = mutableListOf()
        val asks = askKeys()
        val bids = bidKeys()
        val limit: Int = min(depth, min(asks.size, bids.size))
        for (i in 0 until limit) {
            val askEntry = ask.entries.first { it.key == asks[i] }
            val bidEntry = bid.entries.first { it.key == bids[i] }
            toReturn.add(StackPair(askEntry, bidEntry))
        }
        return toReturn
    }

    fun store(rawJson: String) {
        if (history.any { it.raw == rawJson }) return

        val entity = HistoryEntity(rawJson)
        history.add(entity)
        if (!initialized) { preinitialize.add(entity) }
    }

    override fun toString(): String {
        val depth = 5
        val data = getL2(depth)
        if (data.isEmpty()) { return "Pending initialization" }

        var toReturn = "\task[${ask.size}]\t\t\t\tbid[${bid.size}]\n"
        for (i in 0 until depth) {
            val ask = data[i].ask
            val bid = data[i].bid
            toReturn += "${ask.price}\t${ask.amount}\t\t${bid.price}\t${bid.amount}\n"
        }
        return toReturn
    }
}

data class StackPair(val ask: MarketItem, val bid: MarketItem) {
    constructor(askEntry: Map.Entry<BigDecimal, BigDecimal>, bidEntry: Map.Entry<BigDecimal, BigDecimal>):
            this(MarketItem(askEntry), MarketItem(bidEntry))
}

data class MarketItem(val price: BigDecimal, val amount: BigDecimal) {
    constructor(entry: Map.Entry<BigDecimal, BigDecimal>) :
            this(entry.key, entry.value)
}

data class HistoryEntity(val raw: String, val time: Long = System.currentTimeMillis())