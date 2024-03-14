package ru.my.task.libraryreaders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.my.task.libraryreaders.model.ReaderCard;

public interface ReaderCardRepository extends JpaRepository<ReaderCard, Long> {
}