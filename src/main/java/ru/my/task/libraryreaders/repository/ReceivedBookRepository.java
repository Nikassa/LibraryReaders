package ru.my.task.libraryreaders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.my.task.libraryreaders.model.ReaderCard;
import ru.my.task.libraryreaders.model.ReceivedBook;

import java.util.List;

public interface ReceivedBookRepository extends JpaRepository<ReceivedBook, Long> {
    List<ReceivedBook> findByReaderCardId(ReaderCard readerCardId);
}