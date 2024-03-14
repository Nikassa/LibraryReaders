package ru.my.task.libraryreaders.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;

@Getter
public class ReceivedBookDTO {

    private Long id;
    private String bookName;
    private String returned;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date dateBookReceived;
    private Long readerCardId;

    @Override
    public String toString() {
        return "ReceivedBookDTO{" +
                ", id='" + id + '\'' +
                ", bookName='" + bookName + '\'' +
                ", returned='" + returned + '\'' +
                ", dateBookReceived=" + dateBookReceived + '\'' +
                ", readerCardId=" + readerCardId + '\'' +
                '}';
    }
}
