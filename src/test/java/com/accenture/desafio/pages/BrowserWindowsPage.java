package com.accenture.desafio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Alerts, Frame &amp; Windows > Browser Windows.
 */
public class BrowserWindowsPage extends BasePage {

    private static final By NEW_WINDOW_BUTTON = By.id("windowButton");
    private static final By SAMPLE_HEADING = By.id("sampleHeading");

    private String originalWindow;

    public BrowserWindowsPage(WebDriver driver) {
        super(driver);
    }

    public void clickNewWindow() {
        originalWindow = driver.getWindowHandle();
        click(NEW_WINDOW_BUTTON);
    }

    public void switchToNewWindow() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        driver.getWindowHandles().stream()
                .filter(handle -> !handle.equals(originalWindow))
                .findFirst()
                .ifPresent(handle -> driver.switchTo().window(handle));
    }

    public int openWindowsCount() {
        return driver.getWindowHandles().size();
    }

    public String sampleHeadingText() {
        return text(SAMPLE_HEADING);
    }

    public void closeNewWindowAndReturn() {
        driver.close();
        driver.switchTo().window(originalWindow);
    }

    public boolean isOnOriginalWindow() {
        return driver.getWindowHandle().equals(originalWindow);
    }
}
