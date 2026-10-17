package com.accenture.desafio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Widgets > Progress Bar.
 */
public class ProgressBarPage extends BasePage {

    private static final By START_STOP_BUTTON = By.id("startStopButton");
    private static final By RESET_BUTTON = By.id("resetButton");
    private static final By PROGRESS_BAR = By.cssSelector("#progressBar [role='progressbar']");

    public ProgressBarPage(WebDriver driver) {
        super(driver);
    }

    public void clickStart() {
        click(START_STOP_BUTTON);
    }

    /**
     * Inicia a barra e a para assim que atingir {@code stopAt}%.
     * O monitoramento roda dentro do navegador (JavaScript) para reagir em milissegundos,
     * evitando a latência de ida e volta do WebDriver.
     */
    public void startAndStopAt(int stopAt) {
        WebElement button = visible(START_STOP_BUTTON);
        scrollTo(button);
        js.executeAsyncScript("""
                const stopAt = arguments[0];
                const done = arguments[arguments.length - 1];
                const button = document.querySelector('#startStopButton');
                const bar = document.querySelector('#progressBar [role="progressbar"]');
                button.click();
                const timer = setInterval(() => {
                    if (parseInt(bar.getAttribute('aria-valuenow'), 10) >= stopAt) {
                        button.click();
                        clearInterval(timer);
                        done();
                    }
                }, 5);
                """, stopAt);
    }

    public int currentValue() {
        return Integer.parseInt(present(PROGRESS_BAR).getAttribute("aria-valuenow"));
    }

    public void waitUntilComplete() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(d -> currentValue() == 100 && !d.findElements(RESET_BUTTON).isEmpty());
    }

    public void clickReset() {
        click(RESET_BUTTON);
    }
}
