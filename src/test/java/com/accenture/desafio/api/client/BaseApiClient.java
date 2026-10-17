package com.accenture.desafio.api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Configuração base comum a todos os clients da API demoqa.
 */
public abstract class BaseApiClient {

    protected static final String BASE_URI = "https://demoqa.com";

    private static final RequestSpecification SPEC = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .log(LogDetail.METHOD)
            .log(LogDetail.URI)
            .build();

    protected RequestSpecification request() {
        return given().spec(SPEC);
    }

    protected RequestSpecification authenticatedRequest(String token) {
        return request().auth().oauth2(token);
    }
}
