package framework.baseclass.apis;

import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * AbstractWebEndpoint class is parent implementation of all abstract API methods
 * it contains all generic methods and return response in different structured
 * @author Deepak.Rathod
 */
public class AbstractWebEndpoint {

    protected static final Logger LOGGER = LogManager.getLogger();
    // Default headers using in all API
    public static final String USER_KEY = "uid";
    protected static final String I_PLANET_DIRECTORY_KEY = "iPlanetDirectoryPro";
    public static final String X_REQUEST_ID_KEY = "X-Request-Id";

    protected RequestSpecification requestSpecification;

    public AbstractWebEndpoint() {
    }

    public AbstractWebEndpoint(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    /**
     * A helper method to use rest assured to extract validatable Response as desired object type.
     * Return generic class type
     */
    public static <T> T extractAsDto(ValidatableResponse validatableResponse, Class<T> dtoClass) {
        return validatableResponse.extract().as(dtoClass, ObjectMapperType.JACKSON_2);
    }

    /**
     * A helper method to use rest assured to extract validatable response as desired type reference object type.
     * Return generic class type
     */
    public static <T> T extractAsDto(ValidatableResponse validatableResponse, TypeRef<T> type) {
        return validatableResponse.extract().as(type.getTypeAsClass());
    }

    /**
     * A helper method to use rest assured to extract validatable response as desired object list.
     * Return generic list of given class type
     */
    public static <T> List<T> extractAsDtoList(ValidatableResponse validatableResponse, Class<T> dtoClass) {
        return validatableResponse.extract().body().jsonPath().getList("", dtoClass);
    }

    /**
     * A helper method to use rest assured to extract json path.
     * Return JsonPath
     */
    public static JsonPath extractAsJsonPath(ValidatableResponse validatableResponse) {
        return validatableResponse.extract().body().jsonPath();
    }

    public RequestSpecification getRequestSpecification() {
        return this.requestSpecification;
    }

    /**
     * Execute request.
     * @param method - method, e.g. POST, GET, etc.
     * @param path - path to endpoint, such as foo/bar
     * @param body the payload body to be sent in patch
     * @param statusCode expected status code
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse makeRequest(String method, String path, Object body, int statusCode, Object... params) {
        return this.makeRequest(
            this.getRequestSpecification(),
            method,
            path,
            body,
            statusCode,
            params);
    }

    /**
     * Execute request.
     * @param requestSpecification - RequestSpecification
     * @param method - method, e.g. POST, GET, etc.
     * @param path - path to endpoint, such as foo/bar
     * @param body the payload body to be sent in patch
     * @param statusCode expected status code
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse makeRequest(
        RequestSpecification requestSpecification,
        String method,
        String path,
        Object body,
        int statusCode,
        Object... params) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);
        if (body != null) {
            specBuilder.setBody(body);
        }
        return
            given()
                .spec(specBuilder.build())
                .when()
                .request(
                    method,
                    path,
                    params)
                .then()
                .statusCode(statusCode);
    }

    /**
     * Execute POST request.
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in patch
     * @param pathParams path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public <T> T post(String path, Object bodyPayload, Class<T> responseClass, Object... pathParams) {
        return this.post(path, bodyPayload, pathParams)
            .extract().as(responseClass, ObjectMapperType.JACKSON_2);
    }

    /**
     * Execute POST request.
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in patch
     * @param pathParams path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse post(String path, Object bodyPayload, Object... pathParams) {
        return this.post(this.getRequestSpecification(), path, bodyPayload, pathParams);
    }

    /**
     * Execute POST request.
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in patch
     * @param pathParams path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse post(RequestSpecification requestSpecification, String path, Object bodyPayload,
        Object... pathParams) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);

        if (bodyPayload != null) {
            specBuilder.setBody(bodyPayload);
        }
        return given()
            .spec(specBuilder.build())
            .when()
            .post(path, pathParams)
            .then();
    }

    /**
     * Execute HEAD request.
     * @param path - path to endpoint, such as foo/bar
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse head(String path, Object... params) {
        return given()
            .spec(getRequestSpecification())
            .when()
            .head(path, params)
            .then();
    }

    /**
     * Execute GET request.
     * @param path - path to endpoint, such as foo/bar
     * @param responseClass - Class&lt;T&gt; - tell RestAssured to deserialize response into desired object.
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @param <T> any custom object
     * @return deserialized into T object
     */
    public <T> T get(String path, Class<T> responseClass, Object... params) {
        return this.get(path, params)
            .extract().as(responseClass, ObjectMapperType.JACKSON_2);
    }

    /**
     * Execute GET request.
     * @param path - path to endpoint, such as foo/bar
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse get(String path, Object... params) {
    	return get(this.getRequestSpecification(), path, params);
    }
    
    public Response getResponse(String path, Object... params) {
    	return getResponse(this.getRequestSpecification(), path, params);
    }
    
    public Response getResponse(RequestSpecification requestSpecification, String path, Object... params) {
        LOGGER.debug("Send GET request to url [{}]", path);
        return given()
            .spec(requestSpecification)
            .when()
            .get(path, params)
            .andReturn();
    }
    /**
     * Execute GET request.
     * @param requestSpecification RequestSpecification - holds rest assured information about request
     * @param path - path to endpoint, such as foo/bar
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse get(RequestSpecification requestSpecification, String path, Object... params) {
        LOGGER.debug("Send GET request to url [{}]", path);
        return given()
            .spec(requestSpecification)
            .when()
            .get(path, params)
            .then();
    }

    /**
     * Execute GET request.
     * @param requestSpecification RequestSpecification - holds rest assured information about request
     * @param config - RestAssuredConfig
     * @param path - path to endpoint, such as foo/bar
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse get(RequestSpecification requestSpecification, RestAssuredConfig config, String path,
        Object... params) {
        return given()
            .spec(requestSpecification)
            .config(config)
            .when()
            .get(path, params)
            .then();
    }

    /**
     * Execute GET request.
     * @param path - path to endpoint, such as foo/bar
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse get(RequestSpecification requestSpecification, String path,
        Map<String, ?> queryParams, Object... params) {
        return given()
            .spec(requestSpecification)
            .when()
            .queryParams(queryParams)
            .get(path, params)
            .then();
    }

    /**
     * Execute DELETE request.
     * @param path - path to endpoint, such as foo/bar
     * @param responseClass - Class&lt;T&gt; - tell RestAssured to deserialize response into desired object.
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @param <T> any custom object
     * @return deserialized into T object
     */
    public <T> T delete(String path, Class<T> responseClass, Object... params) {
        return this.delete(path, params)
            .extract().as(responseClass, ObjectMapperType.JACKSON_2);
    }

    /**
     * Execute DELETE request.
     * @param path - path to endpoint, such as foo/bar
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse delete(String path, Object... params) {
        return delete(this.getRequestSpecification() , path, params);
    }

    /**
     * Execute DELETE request.
     * @param requestSpecification RequestSpecification - holds rest assured information about request
     * @param path - path to endpoint, such as foo/bar
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse delete(RequestSpecification requestSpecification, String path, Object... params) {
        return given()
            .spec(requestSpecification)
            .when()
            .delete(path, params)
            .then();
    }

    /**
     * Execute DELETE request.
     * @param requestSpecification RequestSpecification - holds rest assured information about request
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in put
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse deleteWithBody(RequestSpecification requestSpecification, String path,
        Object bodyPayload, Object... params) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.addRequestSpecification(requestSpecification);

        if (bodyPayload != null) {
            specBuilder.setBody(bodyPayload);
        }
        return given()
            .spec(specBuilder.build())
            .when()
            .delete(path, params)
            .then();
    }

    /**
     * Execute PUT request.
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in patch
     * @param responseClass - Class&lt;T&gt; - tell RestAssured to deserialize response into desired object.
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @param <T> any custom object
     * @return deserialized into T object
     */
    public <T> T put(String path, Object bodyPayload, Class<T> responseClass, Object... params) {
        return this.put(path, bodyPayload, params)
            .extract().as(responseClass, ObjectMapperType.JACKSON_2);
    }

    /**
     * Execute PUT request.
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in patch
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse put(String path, Object bodyPayload, Object... params) {
        return this.put(this.requestSpecification, path, bodyPayload, params);
    }

    /**
     * Execute PUT request.
     * @param requestSpecification RequestSpecification - holds rest assured information about request
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in put
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse put(RequestSpecification requestSpecification, String path, Object bodyPayload,
        Object... params) {
        return given()
            .spec(requestSpecification)
            .when()
            .body(bodyPayload)
            .put(path, params)
            .then();
    }

    /**
     * Execute PUT request.
     * @param requestSpecification RequestSpecification - holds rest assured information about request
     * @param path - path to endpoint, such as foo/bar
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse put(RequestSpecification requestSpecification, String path, Object... params) {
        return given()
            .spec(requestSpecification)
            .when()
            .put(path, params)
            .then();
    }

    /**
     * Execute PATCH request.
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in patch
     * @param responseClass - Class&lt;T&gt; - tell RestAssured to deserialize response into desired object.
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @param <T> any custom object
     * @return deserialized into T object
     */
    public <T> T patch(String path, Object bodyPayload, Class<T> responseClass, Object... params) {
        return this.patch(path, bodyPayload, params)
            .extract().as(responseClass, ObjectMapperType.JACKSON_2);
    }

    /**
     * Execute PATCH request.
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in patch
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse patch(String path, Object bodyPayload, Object... params) {
        return this.patch(this.requestSpecification, path, bodyPayload, params);
    }

    /**
     * Execute PATCH request.
     * @param requestSpecification RequestSpecification - holds rest assured information about request
     * @param path - path to endpoint, such as foo/bar
     * @param bodyPayload the payload body to be sent in patch
     * @param params path params, for example if path contains /foo/{pathparam1}/bar/{pathparam2},
     *     then parameters will replaced in path.
     * @return ValidatableResponse
     */
    public ValidatableResponse patch(RequestSpecification requestSpecification, String path, Object bodyPayload,
        Object... params) {
        return given()
            .spec(requestSpecification)
            .when()
            .body(bodyPayload)
            .patch(path, params)
            .then();

    }
}
