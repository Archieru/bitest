package tests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.message.MessageType;
import io.qameta.allure.Step;
import org.testng.annotations.Test;
import pojo.Error;
import pojo.ErrorResponse;

public class TestSocketWSS extends TestNGCitrusTestRunner {

    private TestContext context;

    @Step("Step for test")
    @Test(description = "Description for test", enabled=true)
    @CitrusTest
    public void getTestActionsWSS() {
        this.context = citrus.createTestContext();

        send(action -> action
                .endpoint("wssClient")
        );

        receive(action -> action
                .endpoint("wssClient")
                .messageType(MessageType.JSON)
                .payload(getJsonDataResponseWSS(), "objectMapper")
        );
    }

    public ErrorResponse getJsonDataResponseWSS(){
        ErrorResponse errorResponse = new ErrorResponse();

        Error error = new Error();
        error.setCode(2);
//        error.setMsg("EOF while parsing a value at line 1 column 0");
        error.setMsg("Invalid JSON: EOF while parsing a value at line 1 column 0");
        errorResponse.setError(error);

        return errorResponse;
    }
}