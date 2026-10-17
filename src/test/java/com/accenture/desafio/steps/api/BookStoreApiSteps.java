package com.accenture.desafio.steps.api;

import com.accenture.desafio.api.ApiContext;
import com.accenture.desafio.api.client.AccountClient;
import com.accenture.desafio.api.client.BookStoreClient;
import com.accenture.desafio.api.model.AddBooksRequest;
import com.accenture.desafio.api.model.Book;
import com.accenture.desafio.api.model.BooksResponse;
import com.accenture.desafio.api.model.CreateUserResponse;
import com.accenture.desafio.api.model.TokenResponse;
import com.accenture.desafio.api.model.UserDetailsResponse;
import com.accenture.desafio.api.model.UserRequest;
import com.accenture.desafio.utils.DataGenerator;
import io.cucumber.java.After;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookStoreApiSteps {

    private final ApiContext ctx;
    private final AccountClient accountClient = new AccountClient();
    private final BookStoreClient bookStoreClient = new BookStoreClient();

    public BookStoreApiSteps(ApiContext ctx) {
        this.ctx = ctx;
    }

    @Dado("que eu crio um novo usuário com credenciais válidas")
    public void criarUsuario() {
        ctx.credentials = new UserRequest(DataGenerator.username(), DataGenerator.strongPassword());

        Response response = accountClient.createUser(ctx.credentials);

        assertThat(response.statusCode()).as("status do cadastro: " + response.asString()).isEqualTo(201);
        CreateUserResponse body = response.as(CreateUserResponse.class);
        assertThat(body.username()).isEqualTo(ctx.credentials.userName());
        assertThat(body.userID()).isNotBlank();
        ctx.userId = body.userID();
    }

    @Quando("eu gero um token de acesso para o usuário")
    public void gerarToken() {
        Response response = accountClient.generateToken(ctx.credentials);

        assertThat(response.statusCode()).isEqualTo(200);
        TokenResponse body = response.as(TokenResponse.class);
        assertThat(body.status()).isEqualTo("Success");
        assertThat(body.token()).isNotBlank();
        ctx.token = body.token();
    }

    @Então("o usuário deve estar autorizado")
    public void validarAutorizacao() {
        Response response = accountClient.authorized(ctx.credentials);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.as(Boolean.class)).as("usuário autorizado").isTrue();
    }

    @Quando("eu listo os livros disponíveis")
    public void listarLivros() {
        Response response = bookStoreClient.listBooks();

        assertThat(response.statusCode()).isEqualTo(200);
        ctx.availableBooks = response.as(BooksResponse.class).books();
        assertThat(ctx.availableBooks).as("livros disponíveis").isNotEmpty();
    }

    @E("eu alugo {int} livros de livre escolha")
    public void alugarLivros(int quantidade) {
        List<Book> embaralhados = new ArrayList<>(ctx.availableBooks);
        Collections.shuffle(embaralhados);
        ctx.chosenBooks = embaralhados.subList(0, quantidade);

        List<AddBooksRequest.Isbn> isbns = ctx.chosenBooks.stream()
                .map(book -> new AddBooksRequest.Isbn(book.isbn()))
                .toList();

        Response response = bookStoreClient.addBooks(new AddBooksRequest(ctx.userId, isbns), ctx.token);

        assertThat(response.statusCode()).as("status do aluguel: " + response.asString()).isEqualTo(201);
        assertThat(response.jsonPath().getList("books.isbn", String.class))
                .containsExactlyInAnyOrderElementsOf(isbns.stream().map(AddBooksRequest.Isbn::isbn).toList());
    }

    @Então("os detalhes do usuário devem conter os livros alugados")
    public void validarDetalhesUsuario() {
        Response response = accountClient.getUser(ctx.userId, ctx.token);

        assertThat(response.statusCode()).isEqualTo(200);
        UserDetailsResponse body = response.as(UserDetailsResponse.class);
        assertThat(body.userId()).isEqualTo(ctx.userId);
        assertThat(body.username()).isEqualTo(ctx.credentials.userName());
        assertThat(body.books()).extracting(Book::isbn)
                .containsExactlyInAnyOrderElementsOf(ctx.chosenBooks.stream().map(Book::isbn).toList());

        System.out.println("Usuário " + body.username() + " alugou: "
                + body.books().stream().map(Book::title).toList());
    }

    /** Remove o usuário criado para não deixar lixo na base pública do demoqa. */
    @After("@api")
    public void removerUsuario() {
        if (ctx.userId != null && ctx.token != null) {
            accountClient.deleteUser(ctx.userId, ctx.token);
        }
    }
}
