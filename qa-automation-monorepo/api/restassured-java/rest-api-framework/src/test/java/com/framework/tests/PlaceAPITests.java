package com.framework.tests;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.constants.StatusCodes;
import com.framework.core.RetryAnalyzer;
import com.framework.dataprovider.TestDataProvider;
import com.framework.models.request.AddPlaceRequest;
import com.framework.models.request.Location;
import com.framework.models.response.AddPlaceResponse;
import com.framework.reporting.AllureLogger;
import com.framework.services.PlaceService;
import com.framework.utils.ResponseHelper;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * Places API CRUD tests — data-driven via external JSON.
 *
 * Tests: Add place → Update place → Get place → Delete place.
 */
@Epic("Maps & Places")
@Feature("Places API")
public class PlaceAPITests extends BaseTest {

    @Test(dataProvider = "placeData", dataProviderClass = TestDataProvider.class,
            groups = {"regression"}, retryAnalyzer = RetryAnalyzer.class)
    @Story("Complete CRUD flow for a place")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Data-driven: Add → Update → Get → Delete place")
    @SuppressWarnings("unchecked")
    public void placeCRUDFlow(Map<String, Object> testData) {
        String name = (String) testData.get("name");

        AllureLogger.addParameter("placeName", name);

        PlaceService places = new PlaceService();

        // Step 1: Add Place
        AllureLogger.step("Adding place: " + name);
        AddPlaceResponse addResponse = places.addPlace(testData)
                .assertStatusCode(StatusCodes.OK)
                .as(AddPlaceResponse.class);
        String placeId = addResponse.getPlaceId();
        assertThat(placeId).as("Place ID should not be null").isNotNull();
        assertThat(addResponse.getScope()).isEqualTo("APP");
        log.info("Place added with ID: {}", placeId);

        // Step 2: Update Place
        String updatedAddress = "71 winter walk, USA";
        AllureLogger.step("Updating place address to: " + updatedAddress);
        places.updatePlace(placeId, updatedAddress).assertStatusCode(StatusCodes.OK);
        log.info("Place updated successfully");

        // Step 3: Get Place and verify update
        AllureLogger.step("Getting place to verify update: " + placeId);
        ResponseHelper getHelper = places.getPlace(placeId);
        getHelper.assertStatusCode(StatusCodes.OK);
        String actualAddress = getHelper.extractString("address");
        assertThat(actualAddress).isEqualTo(updatedAddress);
        log.info("Place address verified after update: {}", actualAddress);

        // Step 4: Delete Place
        AllureLogger.step("Deleting place: " + placeId);
        places.deletePlace(placeId).assertStatusCode(StatusCodes.OK);
        log.info("Place {} deleted successfully", placeId);
    }

    @Test(groups = {"smoke"}, retryAnalyzer = RetryAnalyzer.class)
    @Story("Add a place using POJO")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Smoke: Add a place using POJO serialization and verify response")
    public void smokeTest_addPlaceWithPojo() {
        PlaceService places = new PlaceService();

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

        AddPlaceResponse response = places.addPlace(request)
                .assertStatusCode(StatusCodes.OK)
                .as(AddPlaceResponse.class);
        assertThat(response.getPlaceId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo("OK");
        log.info("Smoke test — Place added: id={}, scope={}", response.getPlaceId(), response.getScope());

        // Cleanup — delete place
        places.deletePlace(response.getPlaceId()).assertStatusCode(StatusCodes.OK);
    }
}
