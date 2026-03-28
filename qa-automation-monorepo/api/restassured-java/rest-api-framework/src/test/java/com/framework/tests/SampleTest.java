package com.framework.tests;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;

import com.framework.models.request.AddPlaceRequest;
import com.framework.models.request.Location;
import com.framework.models.response.AddPlaceResponse;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;

public class SampleTest {
    private static String placeId;
    // @Test()
    public void allAPI() {
        //given - input details
       //when - perform the action
       //then - verify the output
       RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given()
         .log().all()
         .queryParam("key", "qaclick123")
         .header("Content-Type", "application/json")
         .body(payload())
            .when()
            .post("maps/api/place/add/json")
            .then()
            .assertThat().statusCode(200)
            .body("scope", equalTo("APP"))
            .header("server", "Apache/2.4.52 (Ubuntu)")
            .extract().response().asString();
        System.out.println(response);
        //JSON path takes input as string and converts it into JSON object and 
        // then we can use it to extract values from the response
        JsonPath js = new JsonPath(response);
        placeId = js.getString("place_id");
        System.out.println("Place ID: " + placeId);
        System.out.println("=======================End OF POST API=======================");

        //update place
        String updatePlaceResponse = given()
            .queryParam("key", "qaclick123")
            .header("Content-Type", "application/json")
            .body(UpdatePayload(placeId))
            .when()
            .put("maps/api/place/update/json")
            .then()
            .assertThat().statusCode(200)
            .extract().response().asString();
        System.out.println(updatePlaceResponse);
        JsonPath jsUpdate = new JsonPath(updatePlaceResponse);
        String messageString = jsUpdate.getString("msg");
        System.out.println(messageString);
        System.out.println("=======================End OF UPDATE API=======================");
        //get place
        String getPlaceResponse = given()
            .queryParam("key", "qaclick123")
            .queryParam("place_id", placeId)
            .when()
            .get("maps/api/place/get/json")
            .then()
            .assertThat().statusCode(200)
            .extract().response().asString();
        System.out.println(getPlaceResponse);
        JsonPath jsGet = new JsonPath(getPlaceResponse);
        String getactualAddress = jsGet.getString("address");
        System.out.println(getactualAddress);
        System.out.println("=======================End OF GET API=======================");
        //delete place
        String deletePlaceResponse = given()
            .queryParam("key", "qaclick123")
            .body(deletePayload(placeId))
            .when()
            .delete("maps/api/place/delete/json")
            .then()
            .assertThat().statusCode(200)
            .extract().response().asString();
        System.out.println(deletePlaceResponse);
    }
    // @Test()
    public void complexJson() {
        JsonPath js = new JsonPath(getComplexJsonAsString());
        int coursesCount = js.getInt("courses.size()");
        System.out.println("Number of courses: " + coursesCount);
        String firstCourseTitle = js.getString("courses[0].title");
        System.out.println("Title of the first course: " + firstCourseTitle);
        int totalPrice = 0;
        for (int i = 0; i < coursesCount; i++) {
            String title = js.getString("courses[" + i + "].title");
            int price = js.getInt("courses[" + i + "].price");
            int copies = js.getInt("courses[" + i + "].copies");
            System.out.println("Course " + (i + 1) + ": Title=" + title + ", Price=" + price + ", Copies=" + copies);
            totalPrice += price * copies;
        }
        System.out.println("Total Price of all courses: " + totalPrice);
        int totalAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("Total Purchase Amount: " + totalAmount);
        String website = js.getString("dashboard.website");
        System.out.println("Website: " + website); 
    }
    @Test
    public void requestPayloadasPojo(){
        AddPlaceRequest request = new AddPlaceRequest();
        request.setAccuracy(50);
        request.setAddress("29, side layout, cohen 09");
        request.setLanguage("French-IN");
        request.setName("Frontline house");
        request.setPhoneNumber("(+91) 983 893 3937");
        request.setTypes(List.of("shoe park", "shop"));
        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        request.setLocation(location);
        request.setWebsite("http://google.com");
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        AddPlaceResponse response = given()
         .log().all()
         .queryParam("key", "qaclick123")
         .header("Content-Type", "application/json")
         .body(request)
            .when()
            .post("maps/api/place/add/json")
            .then()
            .assertThat().statusCode(200)
            .body("scope", equalTo("APP"))
            .extract().response().as(AddPlaceResponse.class);
        System.out.println(response);
        System.out.println("Place ID: " + response.getPlaceId());
        System.out.println("scope: " + response.getScope());
        System.out.println("reference: " + response.getReference());
        System.out.println("id: " + response.getId());
        System.out.println("status: " + response.getStatus());
    }

    public String payload(){
        return "{\n" + //
                "  \"location\": {\n" + //
                "    \"lat\": -38.383494,\n" + //
                "    \"lng\": 33.427362\n" + //
                "  },\n" + //
                "  \"accuracy\": 50,\n" + //
                "  \"name\": \"Frontline house\",\n" + //
                "  \"phone_number\": \"(+91) 983 893 3937\",\n" + //
                "  \"address\": \"29, side layout, cohen 09\",\n" + //
                "  \"types\": [\n" + //
                "    \"shoe park\",\n" + //
                "    \"shop\"\n" + //
                "  ],\n" + //
                "  \"website\": \"http://google.com\",\n" + //
                "  \"language\": \"French-IN\"\n" + //
                "}";
    }
    public String UpdatePayload(String placeId){
        return "{\n" + //
                        "\"place_id\":\"" + placeId + "\",\n" + //
                        "\"address\":\"71 winter walk, USA\",\n" + //
                        "\"key\":\"qaclick123\"\n" + //
                        "}";
    }
    public String deletePayload(String placeId){
        return "{\n" + //
                        "\"place_id\":\"" + placeId + "\"\n" + //
                        "}";
    }
    public String getComplexJsonAsString(){
        return "{\n" + //
                        "    \"dashboard\": {\n" + //
                        "        \"purchaseAmount\": 910,\n" + //
                        "        \"website\": \"rahulshettyacademy.com\"\n" + //
                        "    },\n" + //
                        "    \"courses\": [\n" + //
                        "        {\n" + //
                        "            \"title\": \"Selenium Python\",\n" + //
                        "            \"price\": 50,\n" + //
                        "            \"copies\": 6\n" + //
                        "        },\n" + //
                        "        {\n" + //
                        "            \"title\": \"Cypress\",\n" + //
                        "            \"price\": 40,\n" + //
                        "            \"copies\": 4\n" + //
                        "        },\n" + //
                        "        {\n" + //
                        "            \"title\": \"RPA\",\n" + //
                        "            \"price\": 45,\n" + //
                        "            \"copies\": 10\n" + //
                        "        }\n" + //
                        "    ]\n" + //
                        "}";
    }
}
/*
what is client, clientId, clientSecret
client: An application or service that interacts with an API to request data or perform actions on behalf of a user. example: bookmyshow app, swiggy app, etc. They use APIs to fetch data and provide services to users.
clientId: A unique identifier assigned to a client application when it registers with an API provider. It is used to identify the application making API requests.
clientSecret: A confidential string associated with a clientId, used to authenticate the client application when requesting access tokens from an API provider. It should be kept secure and not shared publicly, as it is used to verify the identity of the client application and grant it access to protected resources on the API.
scope: The level of access or permissions that a client application has when interacting with an API. It defines what actions the client can perform and what data it can access. For example, a scope might allow a client to read user data but not modify it, or to access certain endpoints of an API while restricting others.
 */