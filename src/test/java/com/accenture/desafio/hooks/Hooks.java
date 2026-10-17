package com.accenture.desafio.hooks;

import com.accenture.desafio.support.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @After
    public void tearDown(Scenario scenario) {
        if (!context.hasDriver()) {
            return;
        }
        try {
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) context.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "falha - " + scenario.getName());
            }
        } finally {
            context.quitDriver();
        }
    }
}
