package com.accenture.desafio.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Book(String isbn, String title, String author, String publisher, Integer pages) {
}
