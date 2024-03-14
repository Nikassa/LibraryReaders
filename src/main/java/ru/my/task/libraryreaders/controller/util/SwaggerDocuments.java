package ru.my.task.libraryreaders.controller.util;

public class SwaggerDocuments {

    public static final String GET_CARD_READER_NOTES = "Параметры запроса: \n" +
            "id - идентификатор" + "\n" +
            "lastName - фамилия" + "\n" +
            "firstName - имя" + "\n" +
            "middleName - отчество" + "\n" +
            "birthDate - дата рождения в формате dd.mm.yyyy" + "\n" +
            "gender - пол" + "\n" +
            "insuranceNumber - СНИЛС в формате ХХХ-ХХХ-ХХХ ХХ";

    public static final String GET_RECEIVED_BOOK_NOTES = "Параметры запроса: \n" +
            "id - идентификатор" + "\n" +
            "bookName - книга" + "\n" +
            "returned - возвращена книга" + "\n" +
            "dateBookReceived - дата получения книги в формате dd.mm.yyyy" + "\n" +
            "readerCardId - идентификатор карты читателя";
}
