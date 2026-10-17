package com.accenture.desafio.api.client;

import com.accenture.desafio.api.model.UserRequest;
import io.restassured.response.Response;

/**
 * Endpoints de /Account/v1.
 */
public class AccountClient extends BaseApiClient {

    public Response createUser(UserRequest user) {
        return request().body(user).post("/Account/v1/User");
    }

    public Response generateToken(UserRequest user) {
        return request().body(user).post("/Account/v1/GenerateToken");
    }

    public Response authorized(UserRequest user) {
        return request().body(user).post("/Account/v1/Authorized");
    }

    public Response getUser(String userId, String token) {
        return authenticatedRequest(token).pathParam("userId", userId).get("/Account/v1/User/{userId}");
    }

    public Response deleteUser(String userId, String token) {
        return authenticatedRequest(token).pathParam("userId", userId).delete("/Account/v1/User/{userId}");
    }
}
