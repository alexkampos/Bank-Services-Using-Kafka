package org.marlow.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.marlow.model.dto.UserActionResponse;
import org.marlow.model.enums.UserAction;
import org.marlow.util.Constants;
import org.marlow.util.TestConstants;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;

import static io.restassured.RestAssured.given;

/*
 * Below tests must run separately from the rest (Are excluded in pom.xml).
 * */

@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EmbeddedKafka(
        partitions = 5,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        }
)
public class UserControllerIntegrationConcurrencyTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = TestConstants.BASE_URL;
        RestAssured.port = port;
    }

    /*
     * Below test demonstrates DepositServiceImpl.handleUserAction() ability
     * to handle multiple deposits on same account by different threads.
     * To make it clearer, you can add Thread.sleep(2000) inside handleUserAction()
     * and see how threads wait the previous one to finish the transaction with database.
     * */
    @RepeatedTest(10)
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenDepositWithConcurrency() throws JSONException {
        JSONObject userActionRequest = new JSONObject();
        userActionRequest.put("userEmail", TestConstants.MOCK_USER_EMAIL);
        userActionRequest.put("userAction", UserAction.DEPOSIT);
        userActionRequest.put("amount", 100);

        UserActionResponse response = given()
                .contentType(ContentType.JSON).body(userActionRequest.toString())
                .when()
                .post(Constants.USER_PERFORM_ACTION_URL)
                .then()
                .log().all().assertThat().statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserActionResponse.class);

        Assertions.assertEquals(response.getResult(), Constants.RESPONSE_OK);
    }

    /*
     * Below test demonstrates WithdrawServiceImpl.handleUserAction() ability
     * to handle multiple withdraws on same account by different threads.
     * To make it clearer, you can add Thread.sleep(2000) inside handleUserAction()
     * and see how threads wait the previous one to finish the transaction with database.
     * */
    @RepeatedTest(11)
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenWithdraw() throws JSONException {

        JSONObject userActionRequest = new JSONObject();
        userActionRequest.put("userEmail", TestConstants.MOCK_USER_EMAIL);
        userActionRequest.put("userAction", UserAction.WITHDRAW);
        userActionRequest.put("amount", 100);

        UserActionResponse response = given()
                .contentType(ContentType.JSON).body(userActionRequest.toString())
                .when()
                .post(Constants.USER_PERFORM_ACTION_URL)
                .then()
                .log().all().assertThat().statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserActionResponse.class);

        Assertions.assertEquals(response.getResult(), Constants.RESPONSE_OK);

    }

}
