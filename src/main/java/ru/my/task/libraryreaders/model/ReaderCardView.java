package ru.my.task.libraryreaders.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ReaderCardView {

    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date birthDate;
    private String gender;
    private String insuranceNumber;
    private List<ReceivedBookView> receivedBooks;

    @Override
    public String toString() {
        return "ReaderCardView{" +
                ", id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", insuranceNumber='" + insuranceNumber + '\'' +
                ", receivedBooks='" + receivedBooks + '\'' +
                '}';
    }
}