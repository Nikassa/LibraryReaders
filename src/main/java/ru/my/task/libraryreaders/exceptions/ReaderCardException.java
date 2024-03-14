package ru.my.task.libraryreaders.exceptions;

public class ReaderCardException extends RuntimeException {

    public ReaderCardException(String message, Long id) {
        super(message + " " + id);
    }
}

