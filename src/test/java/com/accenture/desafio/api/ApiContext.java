package com.accenture.desafio.api;

import com.accenture.desafio.api.model.Book;
import com.accenture.desafio.api.model.UserRequest;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Estado compartilhado entre os steps do fluxo de API (uma instância por cenário).
 */
public class ApiContext {

    public UserRequest credentials;
    public String userId;
    public String token;
    public List<Book> availableBooks = new ArrayList<>();
    public List<Book> chosenBooks = new ArrayList<>();
    public Response lastResponse;
}
