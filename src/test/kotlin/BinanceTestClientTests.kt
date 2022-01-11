import jdk.jfr.Enabled
import org.junit.jupiter.api.Assertions
import org.testng.annotations.Test
import java.math.BigDecimal

class BinanceTestClientTests {
    @Test
    fun `binance generated order book sanity test`() {
        val ticker = "RAREUSDT"
        val binanceOrderBook = BinanceClient().initOrderBook(ticker)
        waitUntil() { binanceOrderBook.initialized }
        println(binanceOrderBook.toString())

        val l1 = binanceOrderBook.getL2(1)
        val ask = l1.first().ask
        val bid = l1.first().bid
        Assertions.assertTrue(ask.price > bid.price, binanceOrderBook.toString())
        Assertions.assertTrue(ask.amount > BigDecimal(0), binanceOrderBook.toString())
        Assertions.assertTrue(bid.amount > BigDecimal(0), binanceOrderBook.toString())
        Assertions.assertTrue(binanceOrderBook.getL2(1200).size <= 1000)
    }

    fun waitUntil(timeout: Long = 5000, action: () -> Boolean): Boolean {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() < startTime + timeout) {
            if (action.invoke()) {
                return true
            }
            Thread.sleep(100)
        }
        return false
    }
}