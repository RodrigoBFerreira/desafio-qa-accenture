package com.accenture.desafio.steps.ui;

import com.accenture.desafio.pages.HomePage;
import com.accenture.desafio.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;

/**
 * Steps de navegação reutilizados por todas as features de UI.
 */
public class NavigationSteps {

    private final HomePage homePage;

    public NavigationSteps(TestContext context) {
        this.homePage = new HomePage(context.getDriver());
    }

    @Dado("que eu acesso o site demoqa")
    public void acessarSite() {
        homePage.open();
    }

    @E("escolho a opção {string} na página inicial")
    public void escolherOpcao(String opcao) {
        homePage.chooseCard(opcao);
    }

    @E("clico no submenu {string}")
    public void clicarSubmenu(String submenu) {
        homePage.chooseSubmenu(submenu);
    }
}
