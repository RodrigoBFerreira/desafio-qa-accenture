package com.accenture.desafio.model;

import com.accenture.desafio.utils.DataGenerator;

/**
 * Registro da tabela Elements > Web Tables.
 */
public record WebTableRecord(String firstName, String lastName, String email, int age, int salary, String department) {

    public static WebTableRecord random() {
        return new WebTableRecord(
                DataGenerator.firstName(),
                DataGenerator.lastName(),
                DataGenerator.uniqueEmail(),
                DataGenerator.age(),
                DataGenerator.salary(),
                DataGenerator.department());
    }

    /** Gera uma versão editada do registro, mantendo o e-mail como identificador. */
    public WebTableRecord edited() {
        return new WebTableRecord(
                DataGenerator.firstName(),
                DataGenerator.lastName(),
                email,
                DataGenerator.age(),
                DataGenerator.salary(),
                DataGenerator.department());
    }
}
