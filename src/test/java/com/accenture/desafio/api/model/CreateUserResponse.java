package com.accenture.desafio.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateUserResponse(String userID, String username, List<Book> books) {
}
