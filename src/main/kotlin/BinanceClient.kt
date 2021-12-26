import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

open class BinanceClient {
    companion object {
        var sharedClient: OkHttpClient = OkHttpClient.Builder()
            .pingInterval(20, TimeUnit.SECONDS)
            .build()

        fun requestWithUrl(url: String): Request {
            return Request.Builder().url(url).build()
        }
    }

    var orderBooks: MutableMap<String, BinanceOrderBook> = mutableMapOf()

    fun initOrderBook(ticker: String, onChange: (text: String) -> Unit = {}): BinanceOrderBook {
        val t = ticker.lowercase()
        val orderBook = BinanceOrderBook()
        orderBooks[t] = orderBook
        sharedClient.newWebSocket(binanceWsRequest(ticker),
            object: WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    orderBook.update(text)
                    onChange.invoke(text)
                }
            } )

        val jsonString = sharedClient.newCall(binanceDepthRequest(ticker)).execute().body!!.string()
        orderBook.initialize(jsonString)
        return orderBook
    }

    protected open fun binanceWsRequest(ticker: String): Request {
        return requestWithUrl("wss://stream.binance.com:9443/stream?streams=${ticker.lowercase()}@depth")
    }

    protected open fun binanceDepthRequest(ticker: String): Request {
        return requestWithUrl("https://api.binance.com/api/v3/depth?symbol=${ticker.uppercase()}&limit=1000")
    }
}