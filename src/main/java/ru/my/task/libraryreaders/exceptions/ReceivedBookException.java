package ru.my.task.libraryreaders.exceptions;

public class ReceivedBookException extends RuntimeException {

    public ReceivedBookException(String message, Long id) {
        super(message + " " + id);
    }
}

