package com.accenture.desafio.pages;

import com.accenture.desafio.model.WebTableRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Elements > Web Tables.
 * As linhas são identificadas pelo e-mail (campo único). A validação usa a caixa de busca; edição e exclusão
 * são feitas com a tabela sem filtro e exibindo todas as linhas.
 */
public class WebTablesPage extends BasePage {

    private static final By ADD_BUTTON = By.id("addNewRecordButton");
    private static final By SEARCH_BOX = By.id("searchBox");
    private static final By FORM_MODAL = By.id("registration-form-modal");
    private static final By FIRST_NAME = By.id("firstName");
    private static final By LAST_NAME = By.id("lastName");
    private static final By EMAIL = By.id("userEmail");
    private static final By AGE = By.id("age");
    private static final By SALARY = By.id("salary");
    private static final By DEPARTMENT = By.id("department");
    private static final By SUBMIT = By.id("submit");

    public WebTablesPage(WebDriver driver) {
        super(driver);
    }

    public void addRecord(WebTableRecord record) {
        click(ADD_BUTTON);
        fillForm(record);
    }

    public void editRecord(String email, WebTableRecord newData) {
        showAllRows();
        click(actionButtonOfRow(email, "edit-record-"));
        fillForm(newData);
    }

    /**
     * A exclusão é feita sem o filtro de busca: no demoqa, excluir dentro de uma visão filtrada pode remover a linha
     * errada. Por isso limpamos a busca e aumentamos a quantidade de linhas por página.
     */
    public void deleteRecord(String email) {
        showAllRows();
        click(actionButtonOfRow(email, "delete-record-"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cellWithText(email)));
    }

    /** Limpa a busca e seleciona a maior opção de linhas por página. */
    private void showAllRows() {
        clearSearch();
        driver.findElements(By.tagName("select")).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .ifPresent(element -> {
                    Select select = new Select(element);
                    List<WebElement> options = select.getOptions();
                    select.selectByIndex(options.size() - 1);
                });
    }

    public boolean isRecordPresent(WebTableRecord record) {
        search(record.email());
        List<String> expected = List.of(record.firstName(), record.lastName(), record.email(),
                String.valueOf(record.age()), String.valueOf(record.salary()), record.department());
        By row = rowOf(record.email());
        if (driver.findElements(row).isEmpty()) {
            return false;
        }
        String rowText = driver.findElement(row).getText();
        return expected.stream().allMatch(rowText::contains);
    }

    public boolean isEmailPresent(String email) {
        search(email);
        return !driver.findElements(cellWithText(email)).isEmpty();
    }

    public void clearSearch() {
        WebElement box = visible(SEARCH_BOX);
        box.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
    }

    private void fillForm(WebTableRecord record) {
        visible(FORM_MODAL);
        type(FIRST_NAME, record.firstName());
        type(LAST_NAME, record.lastName());
        type(EMAIL, record.email());
        type(AGE, String.valueOf(record.age()));
        type(SALARY, String.valueOf(record.salary()));
        type(DEPARTMENT, record.department());
        click(SUBMIT);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(FORM_MODAL));
    }

    private void search(String text) {
        clearSearch();
        visible(SEARCH_BOX).sendKeys(text);
    }

    private By cellWithText(String text) {
        return By.xpath(String.format("//*[(@role='gridcell' or self::td) and normalize-space()='%s']", text));
    }

    /** Linha = ancestral mais próximo da célula que contém os botões de ação. */
    private By rowOf(String email) {
        return By.xpath(String.format(
                "//*[(@role='gridcell' or self::td) and normalize-space()='%s']/ancestor::*[.//span[starts-with(@id,'edit-record-')]][1]",
                email));
    }

    private By actionButtonOfRow(String email, String idPrefix) {
        return By.xpath(String.format(
                "//*[(@role='gridcell' or self::td) and normalize-space()='%s']/ancestor::*[.//span[starts-with(@id,'%s')]][1]//span[starts-with(@id,'%s')]",
                email, idPrefix, idPrefix));
    }
}
