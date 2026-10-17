package com.accenture.desafio.steps.ui;

import com.accenture.desafio.pages.ProgressBarPage;
import com.accenture.desafio.support.TestContext;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import static org.assertj.core.api.Assertions.assertThat;

public class ProgressBarSteps {

    private final ProgressBarPage page;

    public ProgressBarSteps(TestContext context) {
        this.page = new ProgressBarPage(context.getDriver());
    }

    @Quando("eu clico em Start e paro a barra antes de {int}%")
    public void iniciarEParar(int limite) {
        // Para com margem de segurança abaixo do limite
        page.startAndStopAt(limite - 5);
    }

    @Então("o valor da progress bar deve ser menor ou igual a {int}%")
    public void validarValorMaximo(int limite) {
        int valor = page.currentValue();
        System.out.println("Progress bar parada em " + valor + "%");
        assertThat(valor).isGreaterThan(0).isLessThanOrEqualTo(limite);
    }

    @Quando("eu clico em Start novamente e aguardo chegar a 100%")
    public void continuarAte100() {
        page.clickStart();
        page.waitUntilComplete();
        assertThat(page.currentValue()).isEqualTo(100);
    }

    @E("eu reseto a progress bar")
    public void resetar() {
        page.clickReset();
    }

    @Então("o valor da progress bar deve ser {int}%")
    public void validarValor(int esperado) {
        assertThat(page.currentValue()).isEqualTo(esperado);
    }
}
