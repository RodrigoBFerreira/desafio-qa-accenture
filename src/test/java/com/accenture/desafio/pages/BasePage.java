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
 * Comportamentos comuns a todas as páginas: esperas explícitas, cliques resilientes,
 * remoção de anúncios e controle de velocidade (slow motion) para acompanhamento visual.
 */
public abstract class BasePage {

    protected static final String BASE_URL = "https://demoqa.com";

    // Intervalo de pausa (em ms) entre ações para visualização em tela.
    // Pode ser configurado via -DslowMotion=500. Se headless=false, o padrão é 500ms.
    private static final long SLOW_MOTION_MS = resolveSlowMotion();

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final JavascriptExecutor js;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    private static long resolveSlowMotion() {
        String prop = System.getProperty("slowMotion");
        if (prop == null) {
            prop = System.getProperty("slow.motion");
        }
        if (prop != null) {
            if ("false".equalsIgnoreCase(prop)) {
                return 0L;
            }
            if ("true".equalsIgnoreCase(prop)) {
                return 500L;
            }
            try {
                return Long.parseLong(prop);
            } catch (NumberFormatException ignored) {
            }
        }
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        // Se headless for false (navegador visível), pausa padrão de 500ms para permitir visualização humana
        return isHeadless ? 0L : 500L;
    }

    public static void pauseSlowMotion() {
        pause(SLOW_MOTION_MS);
    }

    public static void pause(long millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void pauseBeforeClose() {
        if (SLOW_MOTION_MS > 0) {
            pause(Math.max(1000L, SLOW_MOTION_MS));
        }
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
        pauseSlowMotion();
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
            jsClick(element);
        }
        pauseSlowMotion();
    }

    protected void jsClick(WebElement element) {
        pauseSlowMotion();
        js.executeScript("arguments[0].click();", element);
        pauseSlowMotion();
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        scrollTo(element);
        pauseSlowMotion();
        element.clear();
        element.sendKeys(text);
        pauseSlowMotion();
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
