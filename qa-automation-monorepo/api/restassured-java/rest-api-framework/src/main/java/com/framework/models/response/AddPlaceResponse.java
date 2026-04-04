package com.framework.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPlaceResponse {
    private String status;

    @JsonProperty("place_id")
    private String placeId;

    private String scope;
    private String reference;
    private String id;
}
