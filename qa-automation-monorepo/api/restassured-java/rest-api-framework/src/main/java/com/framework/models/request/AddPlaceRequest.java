package com.framework.models.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPlaceRequest {
    private Location location;
    private int accuracy;
    private String name;
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;
    private List<String> types;
    private String website;
    private String language;
}
