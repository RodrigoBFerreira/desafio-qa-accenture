package com.accenture.desafio.steps.ui;

import com.accenture.desafio.pages.SortablePage;
import com.accenture.desafio.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SortableSteps {

    private static final List<String> ASCENDING_ORDER = List.of("One", "Two", "Three", "Four", "Five", "Six");

    private final SortablePage page;

    public SortableSteps(TestContext context) {
        this.page = new SortablePage(context.getDriver());
    }

    @Quando("eu coloco os elementos na ordem crescente utilizando drag and drop")
    public void colocarEmOrdemCrescente() {
        // Para garantir que o método de drag and drop seja efetivamente exercitado,
        // movemos "Two" sobre "One" e em seguida recolocamos "One" no topo sobre "Two".
        page.dragAndDropTo("Two", "One");
        page.dragAndDropTo("One", "Two");
    }

    @Então("os elementos devem estar em ordem crescente")
    public void validarOrdem() {
        assertThat(page.currentOrder()).containsExactlyElementsOf(ASCENDING_ORDER);
    }
}
