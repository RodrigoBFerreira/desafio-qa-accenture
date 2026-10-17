package com.accenture.desafio.api.model;

import java.util.List;

public record AddBooksRequest(String userId, List<Isbn> collectionOfIsbns) {

    public record Isbn(String isbn) {
    }
}
