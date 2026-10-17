package com.accenture.desafio.support;

import org.openqa.selenium.WebDriver;

/**
 * Contexto compartilhado entre steps e hooks de um mesmo cenário.
 * Injetado automaticamente pelo cucumber-picocontainer (uma instância nova por cenário).
 */
public class TestContext {

    private WebDriver driver;

    public WebDriver getDriver() {
        if (driver == null) {
            driver = DriverFactory.createDriver();
        }
        return driver;
    }

    public boolean hasDriver() {
        return driver != null;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
