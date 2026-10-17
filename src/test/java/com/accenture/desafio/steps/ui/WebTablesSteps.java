package com.accenture.desafio.steps.ui;

import com.accenture.desafio.model.WebTableRecord;
import com.accenture.desafio.pages.WebTablesPage;
import com.accenture.desafio.support.TestContext;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WebTablesSteps {

    private final WebTablesPage page;
    private final List<WebTableRecord> createdRecords = new ArrayList<>();

    public WebTablesSteps(TestContext context) {
        this.page = new WebTablesPage(context.getDriver());
    }

    @Quando("eu crio um novo registro")
    public void criarRegistro() {
        criarRegistros(1);
    }

    @Quando("eu crio {int} novos registros de forma dinâmica")
    public void criarRegistros(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            WebTableRecord record = WebTableRecord.random();
            page.addRecord(record);
            createdRecords.add(record);
        }
    }

    @Então("o novo registro deve ser exibido na tabela")
    public void validarRegistroCriado() {
        assertThat(page.isRecordPresent(last())).as("registro criado na tabela").isTrue();
    }

    @Então("os {int} registros devem ser exibidos na tabela")
    public void validarRegistrosCriados(int quantidade) {
        assertThat(createdRecords).hasSize(quantidade);
        createdRecords.forEach(record ->
                assertThat(page.isRecordPresent(record)).as("registro %s na tabela", record.email()).isTrue());
    }

    @Quando("eu edito o novo registro criado")
    public void editarRegistro() {
        WebTableRecord edited = last().edited();
        page.editRecord(edited.email(), edited);
        createdRecords.set(createdRecords.size() - 1, edited);
    }

    @Então("o registro deve ser exibido com os dados atualizados")
    public void validarRegistroEditado() {
        assertThat(page.isRecordPresent(last())).as("registro editado na tabela").isTrue();
    }

    @Quando("eu deleto o novo registro criado")
    public void deletarRegistro() {
        page.deleteRecord(last().email());
    }

    /**
     * No demoqa, os registros adicionados compartilham o mesmo id interno, então excluir um deles remove
     * todos os registros novos de uma só vez (bug do site). Por isso só excluímos os que ainda existem;
     * o step seguinte garante que nenhum registro criado permaneceu na tabela.
     */
    @Quando("eu deleto todos os registros criados")
    public void deletarTodos() {
        createdRecords.stream()
                .filter(record -> page.isEmailPresent(record.email()))
                .forEach(record -> page.deleteRecord(record.email()));
    }

    @Então("o registro não deve mais ser exibido na tabela")
    public void validarRegistroDeletado() {
        assertThat(page.isEmailPresent(last().email())).isFalse();
    }

    @Então("nenhum dos registros criados deve ser exibido na tabela")
    public void validarTodosDeletados() {
        createdRecords.forEach(record ->
                assertThat(page.isEmailPresent(record.email())).as("registro %s removido", record.email()).isFalse());
        page.clearSearch();
    }

    private WebTableRecord last() {
        return createdRecords.get(createdRecords.size() - 1);
    }
}
