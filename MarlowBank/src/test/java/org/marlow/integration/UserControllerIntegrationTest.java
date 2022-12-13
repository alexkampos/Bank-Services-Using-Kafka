package org.marlow.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.marlow.model.dto.ApiErrorResponse;
import org.marlow.model.dto.UserActionResponse;
import org.marlow.model.enums.UserAction;
import org.marlow.util.Constants;
import org.marlow.util.TestConstants;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.given;

/*
 * Below tests must run separately from the rest (Are excluded in pom.xml).
 * */

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EmbeddedKafka(
        partitions = 5,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        }
)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = TestConstants.BASE_URL;
        RestAssured.port = port;
    }

    @Test
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenDeposit() throws JSONException {
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

    @Test
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenDepositAndUserNotFound() throws JSONException {

        JSONObject userActionRequest = new JSONObject();
        userActionRequest.put("userEmail", TestConstants.MOCK_NOT_EXISTING_EMAIL);
        userActionRequest.put("userAction", UserAction.DEPOSIT);
        userActionRequest.put("amount", 100);

        ApiErrorResponse response = given()
                .contentType(ContentType.JSON).body(userActionRequest.toString())
                .when()
                .post(Constants.USER_PERFORM_ACTION_URL)
                .then()
                .log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ApiErrorResponse.class);

        Assertions.assertEquals(response.getResult(), Constants.RESPONSE_NOK);
        Assertions.assertEquals(response.getMessage(), Constants.EMAIL_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenDepositAndAmountNotPositive() throws JSONException {

        JSONObject userActionRequest = new JSONObject();
        userActionRequest.put("userEmail", TestConstants.MOCK_USER_EMAIL);
        userActionRequest.put("userAction", UserAction.DEPOSIT);
        userActionRequest.put("amount", 0);

        ApiErrorResponse response = given()
                .contentType(ContentType.JSON).body(userActionRequest.toString())
                .when()
                .post(Constants.USER_PERFORM_ACTION_URL)
                .then()
                .log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ApiErrorResponse.class);

        Assertions.assertEquals(response.getResult(), Constants.RESPONSE_NOK);
        Assertions.assertEquals(response.getMessage(), Constants.AMOUNT_NOT_POSITIVE);
    }

    @Test
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenWithdraw() throws JSONException {

        JSONObject userActionRequest = new JSONObject();
        userActionRequest.put("userEmail", TestConstants.MOCK_USER_EMAIL);
        userActionRequest.put("userAction", UserAction.WITHDRAW);
        userActionRequest.put("amount", 1450);

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

    @Test
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenWithdrawAndAmountNotPositive() throws JSONException {

        JSONObject userActionRequest = new JSONObject();
        userActionRequest.put("userEmail", TestConstants.MOCK_USER_EMAIL);
        userActionRequest.put("userAction", UserAction.WITHDRAW);
        userActionRequest.put("amount", 0);

        ApiErrorResponse response = given()
                .contentType(ContentType.JSON).body(userActionRequest.toString())
                .when()
                .post(Constants.USER_PERFORM_ACTION_URL)
                .then()
                .log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ApiErrorResponse.class);

        Assertions.assertEquals(response.getResult(), Constants.RESPONSE_NOK);
        Assertions.assertEquals(response.getMessage(), Constants.AMOUNT_NOT_POSITIVE);
    }

    @Test
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenWithdrawAndUserNotFound() throws JSONException {

        JSONObject userActionRequest = new JSONObject();
        userActionRequest.put("userEmail", TestConstants.MOCK_NOT_EXISTING_EMAIL);
        userActionRequest.put("userAction", UserAction.WITHDRAW);
        userActionRequest.put("amount", 100);

        ApiErrorResponse response = given()
                .contentType(ContentType.JSON).body(userActionRequest.toString())
                .when()
                .post(Constants.USER_PERFORM_ACTION_URL)
                .then()
                .log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ApiErrorResponse.class);

        Assertions.assertEquals(response.getResult(), Constants.RESPONSE_NOK);
        Assertions.assertEquals(response.getMessage(), Constants.EMAIL_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = TestConstants.MOCK_USER_EMAIL)
    public void testPerformUserActionWhenWithdrawAndBalanceNotEnough() throws JSONException {

        JSONObject userActionRequest = new JSONObject();
        userActionRequest.put("userEmail", TestConstants.MOCK_USER_EMAIL);
        userActionRequest.put("userAction", UserAction.WITHDRAW);
        userActionRequest.put("amount", 100);

        ApiErrorResponse response = given()
                .contentType(ContentType.JSON).body(userActionRequest.toString())
                .when()
                .post(Constants.USER_PERFORM_ACTION_URL)
                .then()
                .log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ApiErrorResponse.class);

        Assertions.assertEquals(response.getResult(), Constants.RESPONSE_NOK);
        Assertions.assertEquals(response.getMessage(), Constants.BALANCE_NOT_ENOUGH);
    }

}
