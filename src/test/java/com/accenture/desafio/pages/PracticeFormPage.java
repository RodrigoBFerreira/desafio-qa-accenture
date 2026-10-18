package com.accenture.desafio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Forms > Practice Form.
 */
public class PracticeFormPage extends BasePage {

    private static final By FIRST_NAME = By.id("firstName");
    private static final By LAST_NAME = By.id("lastName");
    private static final By EMAIL = By.id("userEmail");
    private static final By MOBILE = By.id("userNumber");
    private static final By DATE_OF_BIRTH = By.id("dateOfBirthInput");
    private static final By MONTH_SELECT = By.cssSelector(".react-datepicker__month-select");
    private static final By YEAR_SELECT = By.cssSelector(".react-datepicker__year-select");
    private static final By SUBJECTS = By.id("subjectsInput");
    private static final By SUBJECT_OPTION = By.cssSelector("[class*='subjects-auto-complete__option']");
    private static final By UPLOAD = By.id("uploadPicture");
    private static final By ADDRESS = By.id("currentAddress");
    private static final By STATE = By.cssSelector("#state input");
    private static final By CITY = By.cssSelector("#city input");
    private static final By SUBMIT = By.id("submit");
    private static final By MODAL_TITLE = By.id("example-modal-sizes-title-lg");
    private static final By CLOSE_MODAL = By.id("closeLargeModal");

    public static final Map<String, List<String>> STATES_AND_CITIES = Map.of(
            "NCR", List.of("Delhi", "Gurgaon", "Noida"),
            "Uttar Pradesh", List.of("Agra", "Lucknow", "Merrut"),
            "Haryana", List.of("Karnal", "Panipat"),
            "Rajasthan", List.of("Jaipur", "Jaiselmer"));

    public PracticeFormPage(WebDriver driver) {
        super(driver);
    }

    public void fillFirstName(String value) {
        type(FIRST_NAME, value);
    }

    public void fillLastName(String value) {
        type(LAST_NAME, value);
    }

    public void fillEmail(String value) {
        type(EMAIL, value);
    }

    /** @param gender Male, Female ou Other */
    public void selectGender(String gender) {
        click(By.xpath(String.format("//label[starts-with(@for,'gender-radio') and normalize-space()='%s']", gender)));
    }

    public void fillMobile(String value) {
        type(MOBILE, value);
    }

    /** @param monthIndex 0 = January ... 11 = December */
    public void selectDateOfBirth(int day, int monthIndex, int year) {
        click(DATE_OF_BIRTH);
        new Select(visible(YEAR_SELECT)).selectByValue(String.valueOf(year));
        pauseSlowMotion();
        new Select(visible(MONTH_SELECT)).selectByIndex(monthIndex);
        pauseSlowMotion();
        click(By.cssSelector(String.format(
                ".react-datepicker__day--%03d:not(.react-datepicker__day--outside-month)", day)));
    }

    public void addSubject(String subject) {
        WebElement input = visible(SUBJECTS);
        scrollTo(input);
        pauseSlowMotion();
        input.sendKeys(subject);
        visible(SUBJECT_OPTION);
        pauseSlowMotion();
        input.sendKeys(Keys.ENTER);
        pauseSlowMotion();
    }

    /** @param hobby Sports, Reading ou Music */
    public void selectHobby(String hobby) {
        click(By.xpath(String.format("//label[starts-with(@for,'hobbies-checkbox') and normalize-space()='%s']", hobby)));
    }

    public void uploadFile(Path file) {
        pauseSlowMotion();
        present(UPLOAD).sendKeys(file.toAbsolutePath().toString());
        pauseSlowMotion();
    }

    public String uploadedFileValue() {
        return present(UPLOAD).getAttribute("value");
    }

    public void fillAddress(String value) {
        type(ADDRESS, value);
    }

    public void selectState(String state) {
        selectFromReactSelect(STATE, state);
    }

    public void selectCity(String city) {
        selectFromReactSelect(CITY, city);
    }

    private void selectFromReactSelect(By input, String value) {
        WebElement element = present(input);
        scrollTo(element);
        pauseSlowMotion();
        element.sendKeys(value);
        pauseSlowMotion();
        element.sendKeys(Keys.ENTER);
        pauseSlowMotion();
    }

    public void submit() {
        removeAds();
        jsClick(present(SUBMIT));
    }

    public boolean isConfirmationModalDisplayed() {
        return visible(MODAL_TITLE).isDisplayed();
    }

    public String modalTitle() {
        return text(MODAL_TITLE);
    }

    /** Lê o valor de uma linha da tabela de resumo exibida no popup (ex.: "Student Name"). */
    public String modalValue(String label) {
        return text(By.xpath(String.format("//div[@class='modal-content']//td[normalize-space()='%s']/following-sibling::td", label)));
    }

    /**
     * Clica em "Close". Atualmente o botão do demoqa não fecha o popup (bug do site, verificado com clique real),
     * então, se o popup continuar aberto, usamos a tecla ESC, que é o fechamento padrão do modal Bootstrap.
     */
    public void closeModal() {
        removeAds();
        click(CLOSE_MODAL);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.invisibilityOfElementLocated(MODAL_TITLE));
        } catch (TimeoutException e) {
            new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        }
    }

    public boolean isModalClosed() {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_TITLE));
    }
}
