package com.framework.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddPlaceResponse {
    private String status;

    @JsonProperty("place_id")
    private String placeId;

    private String scope;
    private String reference;
    private String id;

    public AddPlaceResponse() {
    }

    public AddPlaceResponse(String status, String placeId, String scope, String reference, String id) {
        this.status = status;
        this.placeId = placeId;
        this.scope = scope;
        this.reference = reference;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
