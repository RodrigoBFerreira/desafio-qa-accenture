package com.accenture.desafio.support;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.Map;

/**
 * Cria instâncias do ChromeDriver. O Selenium Manager (Selenium 4.6+) baixa o driver automaticamente.
 * Execução headless: mvn test -Dheadless=true
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--window-size=1920,1080", "--disable-notifications", "--disable-search-engine-choice-screen");
        // Bloqueia imagens para acelerar e reduzir anúncios do demoqa
        options.setExperimentalOption("prefs", Map.of("profile.managed_default_content_settings.images", 2));

        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        return driver;
    }
}
