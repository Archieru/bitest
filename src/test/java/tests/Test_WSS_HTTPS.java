package tests;

import com.consol.citrus.validation.json.JsonMappingValidationCallback;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.message.MessageType;
//import io.qameta.allure.Step;
//import io.qameta.allure.Story;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pojo.Error;
import pojo.ErrorResponse;
import pojo.HttpsResponse;
import java.math.BigDecimal;
import java.util.Map;


public class Test_WSS_HTTPS extends TestNGCitrusTestRunner {

    private TestContext context;
    SoftAssert softAssert = new SoftAssert();

    @Test(description = "Description", enabled=true)
    @CitrusTest
//    @Story("TestWebSockets")
    public void getTestActions() {
        this.context = citrus.createTestContext();
//        context.setVariable("tickerUpper", "RAREUSDT");
//        context.setVariable("tickerLower", "rareusdt");
//
//        String tickerUpper = "RAREUSDT";
//        String tickerLower = "rareusdt";
//
//        context.setVariable("urlHTTPS", "https://api.binance.com/api/v3/depth?symbol=" + tickerUpper + "&limit=1000");
//        context.setVariable("urlWSS", "wss://stream.binance.com:9443/stream?streams=" + tickerLower + "@depth");
//
//        echo("!!!!!" + context.getVariable("urlHTTPS"));
//        echo("!!!!!" + context.getVariable("urlWSS"));

        sendRequestToWSS();
        getResponseAndValidateFromWSS();

        sendRequestToHTTPS();
        getResponseAndValidateFromHTTPS();
    }

//    @Step("Send request to WSS")
    public void sendRequestToWSS() {
        send(action -> action
                .endpoint("wssClient")
        );
    }

//    @Step("Get response from WSS and validate data")
    public void getResponseAndValidateFromWSS() {
        receive(action -> action
                .endpoint("wssClient")
                .messageType(MessageType.JSON)
                .payload(getJsonDataResponseWSS(), "objectMapper")
        );
    }

//    @Step("Send request to HTTPS")
    public void sendRequestToHTTPS() {
        http(action -> action
                .client("httpClient")
                .send()
                .get()
        );
    }

//    @Step("Get response from HTTPS and validate data")
    public void getResponseAndValidateFromHTTPS() {
        http(action -> action
                        .client("httpClient")
                        .receive()
                        .response(HttpStatus.OK)
                        .messageType(MessageType.JSON)
//                .ignore("$.lastUpdateId")
                        .extractFromPayload("$.bids", "currentBids")
                        .extractFromPayload("$.asks", "currentAsks")
                        .validationCallback(new JsonMappingValidationCallback<HttpsResponse>(HttpsResponse.class) {
                            @Override
                            public void validate(HttpsResponse payload, Map<String, Object> map, TestContext testContext) {
                                BigDecimal askPrice = new BigDecimal(payload.getAsks().get(0).get(0));
                                BigDecimal bidPrice = new BigDecimal(payload.getBids().get(0).get(0));
                                BigDecimal askAmount = new BigDecimal(payload.getAsks().get(0).get(1));
                                BigDecimal bidAmount = new BigDecimal(payload.getBids().get(0).get(1));
                                BigDecimal decimal = new BigDecimal("0.0");

//                                Assert.assertEquals(askPrice.compareTo(bidPrice), 1 );
//                                Assert.assertEquals(askAmount.compareTo(decimal), 1);
//                                Assert.assertEquals(bidAmount.compareTo(decimal), 1);
//                                Assert.assertTrue(payload.getAsks().size() <= 1000);
//                                Assert.assertTrue(payload.getBids().size() <= 1000);

                                softAssert.assertEquals(askPrice.compareTo(bidPrice), -1 );
                                softAssert.assertEquals(askAmount.compareTo(decimal), 1);
                                softAssert.assertEquals(bidAmount.compareTo(decimal), -3);
                                softAssert.assertTrue(payload.getAsks().size() <= 1000);
                                softAssert.assertTrue(payload.getBids().size() <= 1000);
                                softAssert.assertAll();
                            }
                        })
        );
        echo("currentBids: ${currentBids}");
        echo("currentAsks: ${currentAsks}");
    }

    public ErrorResponse getJsonDataResponseWSS(){
        ErrorResponse errorResponse = new ErrorResponse();
        Error error = new Error();

        error.setCode(3);
        error.setMsg("Invalid JSON: EOF while parsing a value at line 1 column 0");
        errorResponse.setError(error);

        return errorResponse;
    }

//    public HttpsResponse getJsonDataResponseHTTPS(){
//        HttpsResponse httpsResponse = new HttpsResponse();
//        httpsResponse.setLastUpdateId(0);
//        httpsResponse.setBids(Lists.newArrayList());
//        httpsResponse.setAsks(Lists.newArrayList());
//
//        return httpsResponse;
//    }
}
