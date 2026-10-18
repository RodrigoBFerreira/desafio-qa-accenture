package com.accenture.desafio.steps.ui;

import com.accenture.desafio.pages.BasePage;
import com.accenture.desafio.pages.BrowserWindowsPage;
import com.accenture.desafio.support.TestContext;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import static org.assertj.core.api.Assertions.assertThat;

public class BrowserWindowsSteps {

    private final BrowserWindowsPage page;

    public BrowserWindowsSteps(TestContext context) {
        this.page = new BrowserWindowsPage(context.getDriver());
    }

    @Quando("eu clico no botão New Window")
    public void clicarNewWindow() {
        page.clickNewWindow();
    }

    @Então("uma nova janela deve ser aberta")
    public void validarNovaJanela() {
        page.switchToNewWindow();
        assertThat(page.openWindowsCount()).isEqualTo(2);
    }

    @E("a nova janela deve exibir a mensagem {string}")
    public void validarMensagem(String mensagem) {
        assertThat(page.sampleHeadingText()).isEqualTo(mensagem);
        BasePage.pauseSlowMotion();
    }

    @Quando("eu fecho a nova janela")
    public void fecharJanela() {
        page.closeNewWindowAndReturn();
    }

    @Então("devo voltar para a janela original")
    public void validarJanelaOriginal() {
        assertThat(page.openWindowsCount()).isEqualTo(1);
        assertThat(page.isOnOriginalWindow()).isTrue();
    }
}
