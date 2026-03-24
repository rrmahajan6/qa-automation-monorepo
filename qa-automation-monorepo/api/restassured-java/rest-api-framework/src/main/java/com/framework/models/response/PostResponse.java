package com.framework.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Response DTO for a Post resource.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostResponse {

    private Integer id;
    private Integer userId;
    private String title;
    private String body;
}
