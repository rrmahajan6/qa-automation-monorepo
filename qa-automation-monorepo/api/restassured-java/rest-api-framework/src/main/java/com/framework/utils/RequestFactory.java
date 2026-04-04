package com.framework.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Fluent request builder that encapsulates headers, query params, path params,
 * form params, body, and multipart data — then executes the request and returns
 * a {@link ResponseHelper}.
 *
 * <p>Usage:
 * <pre>
 *   ResponseHelper res = RequestFactory.request()
 *       .withSpec(authSpec)
 *       .withContentType(ContentType.JSON)
 *       .withBody(payload)
 *       .post("/api/orders");
 *
 *   res.assertStatusCode(201);
 *   OrderResponse order = res.as(OrderResponse.class);
 * </pre>
 */
public final class RequestFactory {

    public enum HttpMethod { GET, POST, PUT, PATCH, DELETE }

    private RequestSpecification spec;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> queryParams = new HashMap<>();
    private final Map<String, Object> pathParams = new HashMap<>();
    private final Map<String, Object> formParams = new HashMap<>();
    private final List<MultiPartFile> multiParts = new ArrayList<>();
    private Object body;
    private String contentType;
    private ContentType contentTypeEnum;

    private RequestFactory() {}

    public static RequestFactory request() {
        return new RequestFactory();
    }

    // ─── Configuration ────────────────────────────────────────────────────

    public RequestFactory withSpec(RequestSpecification spec) {
        this.spec = spec;
        return this;
    }

    public RequestFactory withHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public RequestFactory withHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public RequestFactory withQueryParam(String name, Object value) {
        this.queryParams.put(name, value);
        return this;
    }

    public RequestFactory withQueryParams(Map<String, Object> params) {
        this.queryParams.putAll(params);
        return this;
    }

    public RequestFactory withPathParam(String name, Object value) {
        this.pathParams.put(name, value);
        return this;
    }

    public RequestFactory withPathParams(Map<String, Object> params) {
        this.pathParams.putAll(params);
        return this;
    }

    public RequestFactory withParam(String name, Object value) {
        this.formParams.put(name, value);
        return this;
    }

    public RequestFactory withParams(Map<String, Object> params) {
        this.formParams.putAll(params);
        return this;
    }

    public RequestFactory withBody(Object body) {
        this.body = body;
        return this;
    }

    public RequestFactory withContentType(ContentType contentType) {
        this.contentTypeEnum = contentType;
        this.contentType = null;
        return this;
    }

    public RequestFactory withContentType(String contentType) {
        this.contentType = contentType;
        this.contentTypeEnum = null;
        return this;
    }

    public RequestFactory withMultiPart(String controlName, File file) {
        this.multiParts.add(new MultiPartFile(controlName, file));
        return this;
    }

    public RequestFactory withBearerToken(String token) {
        this.headers.put("Authorization", "Bearer " + token);
        return this;
    }

    // ─── HTTP methods ─────────────────────────────────────────────────────

    public ResponseHelper get(String endpoint) {
        return execute(HttpMethod.GET, endpoint);
    }

    public ResponseHelper post(String endpoint) {
        return execute(HttpMethod.POST, endpoint);
    }

    public ResponseHelper put(String endpoint) {
        return execute(HttpMethod.PUT, endpoint);
    }

    public ResponseHelper patch(String endpoint) {
        return execute(HttpMethod.PATCH, endpoint);
    }

    public ResponseHelper delete(String endpoint) {
        return execute(HttpMethod.DELETE, endpoint);
    }

    public ResponseHelper execute(HttpMethod method, String endpoint) {
        RequestSpecification reqSpec = (spec != null) ? given().spec(spec) : given();

        if (contentTypeEnum != null) {
            reqSpec.contentType(contentTypeEnum);
        } else if (contentType != null) {
            reqSpec.contentType(contentType);
        }

        if (!headers.isEmpty()) {
            reqSpec.headers(headers);
        }
        if (!queryParams.isEmpty()) {
            queryParams.forEach(reqSpec::queryParam);
        }
        if (!pathParams.isEmpty()) {
            pathParams.forEach(reqSpec::pathParam);
        }
        if (!formParams.isEmpty()) {
            formParams.forEach(reqSpec::param);
        }
        if (!multiParts.isEmpty()) {
            for (MultiPartFile mp : multiParts) {
                reqSpec.multiPart(mp.controlName, mp.file);
            }
        }
        if (body != null) {
            reqSpec.body(body);
        }

        Response response = switch (method) {
            case GET    -> reqSpec.when().get(endpoint).then().extract().response();
            case POST   -> reqSpec.when().post(endpoint).then().extract().response();
            case PUT    -> reqSpec.when().put(endpoint).then().extract().response();
            case PATCH  -> reqSpec.when().patch(endpoint).then().extract().response();
            case DELETE -> reqSpec.when().delete(endpoint).then().extract().response();
        };

        return new ResponseHelper(response);
    }

    private record MultiPartFile(String controlName, File file) {}
}
