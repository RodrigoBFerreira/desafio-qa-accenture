package com.accenture.desafio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Comportamentos comuns a todas as páginas: esperas explícitas, cliques resilientes e remoção de anúncios.
 */
public abstract class BasePage {

    protected static final String BASE_URL = "https://demoqa.com";

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final JavascriptExecutor js;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement present(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void scrollTo(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    /** Clica no elemento; se algum overlay/anúncio interceptar o clique, usa clique via JavaScript. */
    protected void click(By locator) {
        WebElement element = present(locator);
        scrollTo(element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
            jsClick(element);
        }
    }

    protected void jsClick(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        scrollTo(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    /** O demoqa exibe banners de anúncio fixos que podem cobrir elementos; removemos para estabilizar os testes. */
    protected void removeAds() {
        js.executeScript("""
                document.querySelectorAll('#fixedban, footer, #adplus-anchor, .Advertisement-Section, iframe')
                        .forEach(e => e.remove());
                """);
    }
}
