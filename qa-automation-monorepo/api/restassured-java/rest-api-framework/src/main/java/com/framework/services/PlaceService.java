package com.framework.services;

import java.util.List;
import java.util.Map;

import com.framework.config.ConfigManager;
import com.framework.constants.APIEndpoints;
import com.framework.models.request.AddPlaceRequest;
import com.framework.models.request.Location;
import com.framework.utils.RequestFactory;
import com.framework.utils.RequestFactory.HttpMethod;
import com.framework.utils.ResponseHelper;
import com.framework.utils.SpecBuilder;

import io.restassured.specification.RequestSpecification;

/**
 * Service layer for the Places API.
 *
 * <p>Each method builds the request and returns {@link ResponseHelper} —
 * tests own all assertions and deserialization.
 */
public class PlaceService {

    private final RequestSpecification placesSpec;

    public PlaceService() {
        this.placesSpec = SpecBuilder.placesSpec();
    }

    public PlaceService(RequestSpecification placesSpec) {
        this.placesSpec = placesSpec;
    }

    // ─── CRUD ─────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public ResponseHelper addPlace(Map<String, Object> testData) {
        AddPlaceRequest request = new AddPlaceRequest();
        request.setName((String) testData.get("name"));
        request.setAddress((String) testData.get("address"));
        request.setPhoneNumber((String) testData.get("phoneNumber"));
        request.setLanguage((String) testData.get("language"));
        request.setWebsite((String) testData.get("website"));
        request.setAccuracy(((Number) testData.get("accuracy")).intValue());
        request.setTypes((List<String>) testData.get("types"));

        Location location = new Location();
        location.setLat(((Number) testData.get("lat")).doubleValue());
        location.setLng(((Number) testData.get("lng")).doubleValue());
        request.setLocation(location);

        return addPlace(request);
    }

    public ResponseHelper addPlace(AddPlaceRequest request) {
        return buildRequest()
                .withBody(request)
                .execute(HttpMethod.POST, APIEndpoints.PLACE_ADD);
    }

    public ResponseHelper updatePlace(String placeId, String newAddress) {
        String payload = String.format(
                "{\"place_id\":\"%s\",\"address\":\"%s\",\"key\":\"%s\"}",
                placeId, newAddress,
                ConfigManager.get().placesApiKey());

        return buildRequest()
                .withBody(payload)
                .execute(HttpMethod.PUT, APIEndpoints.PLACE_UPDATE);
    }

    public ResponseHelper getPlace(String placeId) {
        return buildRequest()
                .withQueryParam("place_id", placeId)
                .execute(HttpMethod.GET, APIEndpoints.PLACE_GET);
    }

    public ResponseHelper deletePlace(String placeId) {
        String payload = String.format("{\"place_id\":\"%s\"}", placeId);
        return buildRequest()
                .withBody(payload)
                .execute(HttpMethod.DELETE, APIEndpoints.PLACE_DELETE);
    }

    // ─── Internal ─────────────────────────────────────────────────────────

    private RequestFactory buildRequest() {
        return RequestFactory.request().withSpec(placesSpec);
    }
}
