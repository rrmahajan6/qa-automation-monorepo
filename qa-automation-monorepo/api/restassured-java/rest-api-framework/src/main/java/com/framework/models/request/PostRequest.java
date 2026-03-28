package com.framework.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload DTO for creating / updating a Post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostRequest {

    private Integer userId;
    private String title;
    private String body;

    public static PostRequestBuilder builder() {
        return new PostRequestBuilder();
    }

    public static class PostRequestBuilder {
        private final PostRequest instance = new PostRequest();

        public PostRequestBuilder userId(Integer userId) {
            instance.userId = userId;
            return this;
        }

        public PostRequestBuilder title(String title) {
            instance.title = title;
            return this;
        }

        public PostRequestBuilder body(String body) {
            instance.body = body;
            return this;
        }

        public PostRequest build() {
            return instance;
        }
    }
}
