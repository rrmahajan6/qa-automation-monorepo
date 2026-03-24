package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.constants.APIEndpoints;
import com.framework.constants.StatusCodes;
import com.framework.models.request.UserRequest;
import com.framework.models.response.UserResponse;
import com.framework.reporting.AllureLogger;
import com.framework.utils.JsonUtils;
import com.framework.utils.ResponseHelper;
import com.framework.utils.SchemaValidator;
import com.framework.utils.TestDataFactory;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end CRUD tests for the /users endpoint.
 *
 * Target API: https://jsonplaceholder.typicode.com (free public mock API)
 *
 * Test groups:
 *  smoke      — quick sanity tests
 *  regression — full CRUD coverage
 */
@Epic("User Management")
@Feature("Users API")
public class UserTests extends BaseTest {

    // ─── GET /users ───────────────────────────────────────────────────────

    @Test(groups = {"smoke", "regression"})
    @Story("Get all users")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify GET /users returns a non-empty list with status 200")
    public void getAllUsers_shouldReturn200AndNonEmptyList() {
        AllureLogger.step("Send GET request to /users");

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(APIEndpoints.USERS)
                .then()
                .extract().response();

        ResponseHelper helper = new ResponseHelper(response);

        helper.assertStatusCode(StatusCodes.OK)
              .assertContentType("application/json")
              .assertResponseTimeLessThan(5000);

        List<UserResponse> users = response.jsonPath().getList("", UserResponse.class);

        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getId()).isNotNull();
        assertThat(users.get(0).getName()).isNotBlank();

        AllureLogger.attachJson("Response Body", helper.getBody());
        log.info("Retrieved {} users", users.size());
    }

    // ─── GET /users/{id} ──────────────────────────────────────────────────

    @Test(groups = {"smoke", "regression"})
    @Story("Get user by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /users/1 returns correct user data and passes schema validation")
    public void getUserById_shouldReturnCorrectUser() {
        int userId = 1;
        AllureLogger.addParameter("userId", userId);

        Response response = given()
                .spec(requestSpec)
                .pathParam("id", userId)
                .when()
                .get(APIEndpoints.USER_BY_ID)
                .then()
                .extract().response();

        ResponseHelper helper = new ResponseHelper(response);
        helper.assertStatusCode(StatusCodes.OK);

        UserResponse user = helper.as(UserResponse.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user.getId()).isEqualTo(userId);
        softly.assertThat(user.getName()).isNotBlank();
        softly.assertThat(user.getEmail()).contains("@");
        softly.assertAll();

        SchemaValidator.validate(response, "user-schema.json");
        AllureLogger.attachJson("User Response", helper.getBody());
    }

    @Test(groups = {"regression"})
    @Story("Get user by ID — not found")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users/99999 returns 404")
    public void getUserById_whenNotFound_shouldReturn404() {
        given()
                .spec(requestSpec)
                .pathParam("id", 99999)
                .when()
                .get(APIEndpoints.USER_BY_ID)
                .then()
                .statusCode(StatusCodes.NOT_FOUND);
    }

    // ─── POST /users ──────────────────────────────────────────────────────

    @Test(groups = {"regression"})
    @Story("Create user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /users creates a new user and returns 201 with the resource")
    public void createUser_shouldReturn201WithCreatedResource() {
        UserRequest newUser = TestDataFactory.randomUser();
        AllureLogger.attachJson("Request Payload", JsonUtils.toJson(newUser));

        Response response = given()
                .spec(requestSpec)
                .body(newUser)
                .when()
                .post(APIEndpoints.USERS)
                .then()
                .extract().response();

        ResponseHelper helper = new ResponseHelper(response);
        helper.assertStatusCode(StatusCodes.CREATED);

        UserResponse created = helper.as(UserResponse.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(created.getId()).isNotNull().isPositive();
        softly.assertThat(created.getName()).isEqualTo(newUser.getName());
        softly.assertThat(created.getEmail()).isEqualTo(newUser.getEmail());
        softly.assertAll();

        AllureLogger.attachJson("Created User Response", helper.getBody());
        log.info("Created user with ID: {}", created.getId());
    }

    // ─── PUT /users/{id} ──────────────────────────────────────────────────

    @Test(groups = {"regression"})
    @Story("Update user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify PUT /users/1 updates the user and returns 200")
    public void updateUser_shouldReturn200WithUpdatedData() {
        int userId = 1;
        UserRequest updatedUser = TestDataFactory.randomUser();

        Response response = given()
                .spec(requestSpec)
                .pathParam("id", userId)
                .body(updatedUser)
                .when()
                .put(APIEndpoints.USER_BY_ID)
                .then()
                .extract().response();

        ResponseHelper helper = new ResponseHelper(response);
        helper.assertStatusCode(StatusCodes.OK);

        UserResponse updated = helper.as(UserResponse.class);
        assertThat(updated.getName()).isEqualTo(updatedUser.getName());
    }

    // ─── PATCH /users/{id} ────────────────────────────────────────────────

    @Test(groups = {"regression"})
    @Story("Partial update user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify PATCH /users/1 partially updates the user")
    public void patchUser_shouldReturn200() {
        UserRequest patch = UserRequest.builder()
                .name(TestDataFactory.randomName())
                .build();

        given()
                .spec(requestSpec)
                .pathParam("id", 1)
                .body(patch)
                .when()
                .patch(APIEndpoints.USER_BY_ID)
                .then()
                .statusCode(StatusCodes.OK);
    }

    // ─── DELETE /users/{id} ───────────────────────────────────────────────

    @Test(groups = {"regression"})
    @Story("Delete user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify DELETE /users/1 returns 200")
    public void deleteUser_shouldReturn200() {
        given()
                .spec(requestSpec)
                .pathParam("id", 1)
                .when()
                .delete(APIEndpoints.USER_BY_ID)
                .then()
                .statusCode(StatusCodes.OK);
    }
}
