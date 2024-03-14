package ru.my.task.libraryreaders.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import ru.my.task.libraryreaders.annotation.InsuranceNumber;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "reader_card")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReaderCard {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "readerCardsIdSeq", sequenceName = "reader_cards_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "readerCardsIdSeq")
    private Long id;

    @NotEmpty(message = "Incorrect last name")
    @Column(name = "lastname")
    private String lastName;

    @NotEmpty(message = "Incorrect first name")
    @Column(name = "firstname")
    private String firstName;

    @Column(name = "middlename")
    private String middleName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Pattern(regexp = "male|female")
    @Column(name = "gender")
    private String gender;

    @ApiModelProperty("СНИЛС")
    @InsuranceNumber
    @Column(name = "insurance_number")
    private String insuranceNumber;

    public ReaderCard() {
    }

    public ReaderCard(String lastName, String firstName, String middleName, Date birthDate, String gender, String insuranceNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.insuranceNumber = insuranceNumber;
    }

    public ReaderCard(Long id, String lastName, String firstName, String middleName, Date birthDate, String gender, String insuranceNumber) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.insuranceNumber = insuranceNumber;
    }

    @Override
    public String toString() {
        return "ReaderCard{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", insuranceNumber='" + insuranceNumber +
                '}';
    }

    public String toStringWithoutId() {
        return "ReaderCard{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", insuranceNumber='" + insuranceNumber +
                '}';
    }
}
