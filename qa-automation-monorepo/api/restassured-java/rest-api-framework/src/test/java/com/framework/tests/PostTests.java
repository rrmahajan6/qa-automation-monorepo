package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.constants.APIEndpoints;
import com.framework.constants.StatusCodes;
import com.framework.models.request.PostRequest;
import com.framework.models.response.PostResponse;
import com.framework.reporting.AllureLogger;
import com.framework.utils.JsonUtils;
import com.framework.utils.ResponseHelper;
import com.framework.utils.SchemaValidator;
import com.framework.utils.TestDataFactory;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end CRUD tests for the /posts endpoint.
 */
@Epic("Content Management")
@Feature("Posts API")
public class PostTests extends BaseTest {

    // ─── GET /posts ───────────────────────────────────────────────────────

    @Test(groups = {"smoke", "regression"})
    @Story("Get all posts")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify GET /posts returns HTTP 200 with a non-empty list")
    public void getAllPosts_shouldReturn200() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(APIEndpoints.POSTS)
                .then()
                .extract().response();

        ResponseHelper helper = new ResponseHelper(response);
        helper.assertStatusCode(StatusCodes.OK)
              .assertResponseTimeLessThan(5000);

        List<PostResponse> posts = response.jsonPath().getList("", PostResponse.class);
        assertThat(posts).isNotEmpty();
        log.info("Retrieved {} posts", posts.size());
    }

    // ─── GET /posts/{id} ──────────────────────────────────────────────────

    @Test(dataProvider = "validPostIds", groups = {"regression"})
    @Story("Get post by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Parameterised test — verify GET /posts/{id} for multiple valid IDs")
    public void getPostById_shouldReturnCorrectPost(int postId) {
        AllureLogger.addParameter("postId", postId);

        Response response = given()
                .spec(requestSpec)
                .pathParam("id", postId)
                .when()
                .get(APIEndpoints.POST_BY_ID)
                .then()
                .extract().response();

        ResponseHelper helper = new ResponseHelper(response);
        helper.assertStatusCode(StatusCodes.OK);

        PostResponse post = helper.as(PostResponse.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(post.getId()).isEqualTo(postId);
        softly.assertThat(post.getTitle()).isNotBlank();
        softly.assertThat(post.getBody()).isNotBlank();
        softly.assertThat(post.getUserId()).isPositive();
        softly.assertAll();

        SchemaValidator.validate(response, "post-schema.json");
    }

    @DataProvider(name = "validPostIds")
    public Object[][] validPostIds() {
        return new Object[][] {{1}, {5}, {10}};
    }

    // ─── GET /posts?userId={userId} ───────────────────────────────────────

    @Test(groups = {"regression"})
    @Story("Filter posts by user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filtering posts by userId returns only that user's posts")
    public void getPostsByUser_shouldReturnOnlyUserPosts() {
        int userId = 1;

        Response response = given()
                .spec(requestSpec)
                .queryParam("userId", userId)
                .when()
                .get(APIEndpoints.POSTS)
                .then()
                .extract().response();

        new ResponseHelper(response).assertStatusCode(StatusCodes.OK);

        List<PostResponse> posts = response.jsonPath().getList("", PostResponse.class);
        assertThat(posts).isNotEmpty();
        assertThat(posts).allMatch(p -> p.getUserId().equals(userId),
                "All posts should belong to userId=" + userId);
    }

    // ─── POST /posts ──────────────────────────────────────────────────────

    @Test(groups = {"regression"})
    @Story("Create post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /posts creates a new post and returns 201")
    public void createPost_shouldReturn201WithCreatedResource() {
        PostRequest newPost = TestDataFactory.randomPost(1);
        AllureLogger.attachJson("Request Payload", JsonUtils.toJson(newPost));

        Response response = given()
                .spec(requestSpec)
                .body(newPost)
                .when()
                .post(APIEndpoints.POSTS)
                .then()
                .extract().response();

        ResponseHelper helper = new ResponseHelper(response);
        helper.assertStatusCode(StatusCodes.CREATED);

        PostResponse created = helper.as(PostResponse.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(created.getId()).isNotNull().isPositive();
        softly.assertThat(created.getTitle()).isEqualTo(newPost.getTitle());
        softly.assertThat(created.getBody()).isEqualTo(newPost.getBody());
        softly.assertThat(created.getUserId()).isEqualTo(newPost.getUserId());
        softly.assertAll();

        AllureLogger.attachJson("Created Post", helper.getBody());
    }

    // ─── PUT /posts/{id} ──────────────────────────────────────────────────

    @Test(groups = {"regression"})
    @Story("Update post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify PUT /posts/1 returns 200 with updated data")
    public void updatePost_shouldReturn200() {
        PostRequest updatedPost = TestDataFactory.randomPost(1);

        given()
                .spec(requestSpec)
                .pathParam("id", 1)
                .body(updatedPost)
                .when()
                .put(APIEndpoints.POST_BY_ID)
                .then()
                .statusCode(StatusCodes.OK);
    }

    // ─── DELETE /posts/{id} ───────────────────────────────────────────────

    @Test(groups = {"regression"})
    @Story("Delete post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify DELETE /posts/1 returns 200")
    public void deletePost_shouldReturn200() {
        given()
                .spec(requestSpec)
                .pathParam("id", 1)
                .when()
                .delete(APIEndpoints.POST_BY_ID)
                .then()
                .statusCode(StatusCodes.OK);
    }
}
