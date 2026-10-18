package com.accenture.desafio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

/**
 * Interactions > Sortable (aba List).
 */
public class SortablePage extends BasePage {

    private static final By LIST_ITEMS = By.cssSelector("#demo-tabpane-list .list-group-item");

    public SortablePage(WebDriver driver) {
        super(driver);
    }

    public List<String> currentOrder() {
        visible(LIST_ITEMS);
        return driver.findElements(LIST_ITEMS).stream().map(e -> e.getText().trim()).toList();
    }

    public void dragAndDropTo(String sourceText, String targetText) {
        List<WebElement> items = driver.findElements(LIST_ITEMS);
        WebElement source = items.stream().filter(e -> e.getText().trim().equals(sourceText)).findFirst().orElseThrow();
        WebElement target = items.stream().filter(e -> e.getText().trim().equals(targetText)).findFirst().orElseThrow();
        scrollTo(target);
        new Actions(driver)
                .clickAndHold(source)
                .pause(Duration.ofMillis(300))
                .moveToElement(target)
                .moveByOffset(0, -5)
                .pause(Duration.ofMillis(300))
                .release()
                .perform();
        pause(800);
    }
}
