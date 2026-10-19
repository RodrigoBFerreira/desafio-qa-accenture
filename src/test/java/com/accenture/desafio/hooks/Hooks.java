package com.accenture.desafio.hooks;

import com.accenture.desafio.pages.BasePage;
import com.accenture.desafio.support.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Hooks {

    private final TestContext context;
    private final AtomicInteger stepIndex = new AtomicInteger(1);

    public Hooks(TestContext context) {
        this.context = context;
    }

    /**
     * Tira print após cada passo de UI:
     * 1. Anexa diretamente no relatório HTML oficial do Cucumber (target/cucumber-report/index.html).
     * 2. Salva em disco em target/evidencias/<cenario>/passo_XX.png para fácil consulta.
     */
    @AfterStep
    public void capturarEvidenciaDoPasso(Scenario scenario) {
        if (!context.hasDriver()) {
            return;
        }
        try {
            byte[] screenshot = ((TakesScreenshot) context.getDriver()).getScreenshotAs(OutputType.BYTES);
            int index = stepIndex.getAndIncrement();
            scenario.attach(screenshot, "image/png", String.format("Passo %02d", index));

            salvarEmDisco(scenario.getName(), index, screenshot);
        } catch (Exception ignored) {
            // Em caso de transição de janela ou alerta, não bloqueia o teste
        }
    }

    private void salvarEmDisco(String scenarioName, int index, byte[] screenshot) {
        try {
            String pastaSegura = scenarioName.replaceAll("[^a-zA-Z0-9_-]", "_");
            Path dir = Paths.get("target", "evidencias", pastaSegura);
            Files.createDirectories(dir);
            Path arquivo = dir.resolve(String.format("passo_%02d.png", index));
            Files.write(arquivo, screenshot);
        } catch (IOException ignored) {
        }
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
            } else {
                BasePage.pauseBeforeClose();
            }
        } finally {
            context.quitDriver();
        }
    }
}
