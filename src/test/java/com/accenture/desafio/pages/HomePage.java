package com.accenture.desafio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Página inicial do demoqa e menu lateral de navegação.
 */
public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(BASE_URL + "/");
        visible(By.cssSelector(".card"));
        removeAds();
    }

    public void chooseCard(String cardName) {
        click(By.xpath(String.format("//div[contains(@class,'card')]//h5[normalize-space()='%s']", cardName)));
        removeAds();
    }

    public void chooseSubmenu(String submenu) {
        click(By.xpath(String.format("//li//span[normalize-space()='%s']", submenu)));
        removeAds();
    }
}
