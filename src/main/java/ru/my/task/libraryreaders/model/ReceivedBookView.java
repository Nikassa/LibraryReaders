package ru.my.task.libraryreaders.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReceivedBookView {

    private Long id;
    private String bookName;
    private String returned;
    private Date dateBookReceived;
    private Long readerCardId;

    @Override
    public String toString() {
        return "ReceivedBookView{" +
                ", id='" + id + '\'' +
                ", bookName='" + bookName + '\'' +
                ", returned='" + returned + '\'' +
                ", dateBookReceived=" + dateBookReceived + '\'' +
                ", readerCardId=" + readerCardId + '\'' +
                '}';
    }
}
