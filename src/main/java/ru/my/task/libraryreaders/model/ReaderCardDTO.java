package ru.my.task.libraryreaders.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class ReaderCardDTO {

    private String lastName;
    private String firstName;
    private String middleName;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date birthDate;
    private String gender;
    private String insuranceNumber;
    private List<ReceivedBookDTO> receivedBooks;

    @Override
    public String toString() {
        return "ReaderCardDTO{" +
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