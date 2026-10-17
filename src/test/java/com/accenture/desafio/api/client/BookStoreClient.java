package com.accenture.desafio.api.client;

import com.accenture.desafio.api.model.AddBooksRequest;
import io.restassured.response.Response;

/**
 * Endpoints de /BookStore/v1.
 */
public class BookStoreClient extends BaseApiClient {

    public Response listBooks() {
        return request().get("/BookStore/v1/Books");
    }

    public Response addBooks(AddBooksRequest body, String token) {
        return authenticatedRequest(token).body(body).post("/BookStore/v1/Books");
    }
}
