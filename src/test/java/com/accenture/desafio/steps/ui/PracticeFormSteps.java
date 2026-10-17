package com.accenture.desafio.steps.ui;

import com.accenture.desafio.pages.PracticeFormPage;
import com.accenture.desafio.support.TestContext;
import com.accenture.desafio.utils.DataGenerator;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PracticeFormSteps {

    private static final List<String> SUBJECTS = List.of("Maths", "English", "Physics", "Chemistry", "Biology",
            "Computer Science", "History", "Economics", "Arts");
    private static final List<String> HOBBIES = List.of("Sports", "Reading", "Music");

    private final PracticeFormPage page;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String state;
    private String city;

    public PracticeFormSteps(TestContext context) {
        this.page = new PracticeFormPage(context.getDriver());
    }

    @Quando("eu preencho todo o formulário com valores aleatórios")
    public void preencherFormulario() {
        firstName = DataGenerator.firstName();
        lastName = DataGenerator.lastName();
        email = DataGenerator.uniqueEmail();
        mobile = DataGenerator.mobile();
        state = DataGenerator.pick(new ArrayList<>(PracticeFormPage.STATES_AND_CITIES.keySet()));
        city = DataGenerator.pick(PracticeFormPage.STATES_AND_CITIES.get(state));

        page.fillFirstName(firstName);
        page.fillLastName(lastName);
        page.fillEmail(email);
        page.selectGender(DataGenerator.pick(List.of("Male", "Female", "Other")));
        page.fillMobile(mobile);
        page.selectDateOfBirth(DataGenerator.between(1, 28), DataGenerator.between(0, 11), DataGenerator.between(1970, 2005));
        page.addSubject(DataGenerator.pick(SUBJECTS));
        page.selectHobby(DataGenerator.pick(HOBBIES));
        page.fillAddress(DataGenerator.address());
        page.selectState(state);
        page.selectCity(city);
    }

    @E("faço upload do arquivo {string}")
    public void fazerUpload(String fileName) {
        Path file = Paths.get("src", "test", "resources", "files", fileName);
        assertThat(file).as("arquivo de upload versionado no projeto").exists();
        page.uploadFile(file);
        assertThat(page.uploadedFileValue()).endsWith(fileName);
    }

    @E("submeto o formulário")
    public void submeter() {
        page.submit();
    }

    @Então("um popup de confirmação deve ser exibido com os dados enviados")
    public void validarPopup() {
        assertThat(page.isConfirmationModalDisplayed()).isTrue();
        assertThat(page.modalTitle()).isEqualTo("Thanks for submitting the form");
        assertThat(page.modalValue("Student Name")).isEqualTo(firstName + " " + lastName);
        assertThat(page.modalValue("Student Email")).isEqualTo(email);
        assertThat(page.modalValue("Mobile")).isEqualTo(mobile);
        assertThat(page.modalValue("Picture")).isEqualTo("upload.txt");
        assertThat(page.modalValue("State and City")).isEqualTo(state + " " + city);
    }

    @Quando("eu fecho o popup")
    public void fecharPopup() {
        page.closeModal();
    }

    @Então("o popup não deve mais ser exibido")
    public void validarPopupFechado() {
        assertThat(page.isModalClosed()).isTrue();
    }
}
