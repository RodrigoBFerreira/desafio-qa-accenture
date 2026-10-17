package com.accenture.desafio.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDetailsResponse(String userId, String username, List<Book> books) {
}
