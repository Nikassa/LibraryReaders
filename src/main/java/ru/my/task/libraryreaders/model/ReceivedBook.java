package ru.my.task.libraryreaders.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "received_book")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReceivedBook {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "receivedBooksIdSeq", sequenceName = "received_books_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receivedBooksIdSeq")
    private Long id;

    @ApiModelProperty("Книга")
    @Column(name = "book_name")
    private String bookName;

    @ApiModelProperty("Возвращена книга")
    @Pattern(regexp = "Y|N", message = "ReceivedBook returned must be Y or N")
    @Column(name = "returned")
    private String returned;

    @ApiModelProperty("Дата получения книги")
    @Column(name = "date_book_received", columnDefinition = "DATE")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date dateBookReceived;

    @ApiModelProperty("Карта читателя")
    @ManyToOne
    @JoinColumn(name = "reader_card_id", nullable = false)
    private ReaderCard readerCardId;

    public ReceivedBook() {
    }

    public ReceivedBook(String bookName, String returned, Date dateBookReceived, ReaderCard readerCardId) {
        this.bookName = bookName;
        this.returned = returned;
        this.dateBookReceived = dateBookReceived;
        this.readerCardId = readerCardId;
    }

    public ReceivedBook(Long id, String bookName, String returned, Date dateBookReceived, ReaderCard readerCardId) {
        this.id = id;
        this.bookName = bookName;
        this.returned = returned;
        this.dateBookReceived = dateBookReceived;
        this.readerCardId = readerCardId;
    }
}
