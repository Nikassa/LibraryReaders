package ru.my.task.libraryreaders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.my.task.libraryreaders.exceptions.ReaderCardException;
import ru.my.task.libraryreaders.exceptions.ReceivedBookException;
import ru.my.task.libraryreaders.model.*;
import ru.my.task.libraryreaders.repository.ReaderCardRepository;
import ru.my.task.libraryreaders.repository.ReceivedBookRepository;
import ru.my.task.libraryreaders.service.dto.ReaderCardDTO;
import ru.my.task.libraryreaders.service.dto.ReceivedBookDTO;
import ru.my.task.libraryreaders.service.view.ReaderCardView;
import ru.my.task.libraryreaders.service.view.ReceivedBookView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReaderCardService {

    private final ReaderCardRepository readerCardRepository;
    private final ReceivedBookRepository receivedBookRepository;

    @Transactional(readOnly = true)
    public List<ReaderCardView> getAll() {
        List<ReaderCard> allReaderCards = readerCardRepository.findAll();
        return allReaderCards.stream()
                .map(i -> toView(i, Collections.EMPTY_LIST))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReaderCardView getById(Long id) {
        Optional<ReaderCard> readerCardById = readerCardRepository.findById(id);
        if (readerCardById.isPresent()) {
            ReaderCard readerCard = readerCardById.get();
            List<ReceivedBook> receivedBooksByReaderCardId = receivedBookRepository.findByReaderCardId(readerCard);
            return toView(readerCard, receivedBooksByReaderCardId);
        }
        return null;
    }

    public ReaderCardView create(ReaderCardDTO dto) {
        ReaderCard readerCardForSave = ReaderCard.builder()
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .insuranceNumber(dto.getInsuranceNumber())
                .build();
        ReaderCard savedReaderCard = readerCardRepository.saveAndFlush(readerCardForSave);

        List<ReceivedBook> savedReceivedBooksList = new ArrayList<>();
        if (dto.getReceivedBooks() != null) {
            savedReceivedBooksList = dto.getReceivedBooks().stream()
                    .map(i -> ReceivedBook.builder()
                            .bookName(i.getBookName())
                            .returned(i.getReturned())
                            .dateBookReceived(i.getDateBookReceived())
                            .readerCardId(savedReaderCard)
                            .build())
                    .collect(Collectors.toList())
                    .stream()
                    .map(i -> receivedBookRepository.saveAndFlush(i))
                    .collect(Collectors.toList());
        }
        return toView(savedReaderCard, savedReceivedBooksList);
    }

    public ReaderCardView update(ReaderCardDTO dto, Long id) {
        Optional<ReaderCard> readerCardFromRepository = readerCardRepository.findById(id);
        if (!readerCardFromRepository.isPresent()) {
            throw new ReaderCardException("Cannot find ReaderCard with id: ", id);
        }

        List<ReceivedBook> savedReceivedBooksList = new ArrayList<>();
        if (dto.getReceivedBooks() != null) {
            for (ReceivedBookDTO receivedBookDto : dto.getReceivedBooks()) {
                Optional<ReceivedBook> receivedBookById = receivedBookRepository.findById(receivedBookDto.getId());
                if (!receivedBookById.isPresent()) {
                    throw new ReceivedBookException("Can't find ReceivedBook with id:", id);
                }
                ReceivedBook receivedBookFromRepository = receivedBookById.get();
                if (receivedBookFromRepository.getReaderCardId().getId() != id) {
                    throw new ReceivedBookException("ReceivedBook's ReaderCardId must be the same as the ReaderCard id:", id);
                }
                ReceivedBook receivedBookForSave = ReceivedBook.builder()
                        .id(receivedBookFromRepository.getId())
                        .bookName(receivedBookDto.getBookName())
                        .returned(receivedBookDto.getReturned())
                        .dateBookReceived(receivedBookDto.getDateBookReceived())
                        .readerCardId(receivedBookFromRepository.getReaderCardId())
                        .build();
                ReceivedBook savedReceivedBook = receivedBookRepository.saveAndFlush(receivedBookForSave);
                savedReceivedBooksList.add(savedReceivedBook);
            }
        }

        ReaderCard readerCardForSave = ReaderCard.builder()
                .id(id)
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .insuranceNumber(dto.getInsuranceNumber())
                .build();
        ReaderCard savedReaderCard = readerCardRepository.save(readerCardForSave);
        return toView(savedReaderCard, savedReceivedBooksList);
    }

    public boolean remove(Long id) {
        Optional<ReaderCard> readerCardFromRepository = readerCardRepository.findById(id);
        if (readerCardFromRepository.isPresent()) {
            List<ReceivedBook> receivedBookListByReaderCardId = receivedBookRepository.findByReaderCardId(readerCardFromRepository.get());
            for (ReceivedBook receivedBook : receivedBookListByReaderCardId) {
                receivedBookRepository.delete(receivedBook);
            }
            readerCardRepository.delete(readerCardFromRepository.get());
            return true;
        } else {
            throw new ReaderCardException("Cannot remove ReaderCard with id: ", id);
        }
    }

    private List<ReceivedBookView> toView(List<ReceivedBook> readerCardReceivedBooks) {
        if (readerCardReceivedBooks == null) {
            return null;
        } else {
            return readerCardReceivedBooks.stream()
                    .map(i -> ReceivedBookView.builder()
                            .id(i.getId())
                            .bookName(i.getBookName())
                            .returned(i.getReturned())
                            .dateBookReceived(i.getDateBookReceived())
                            .readerCardId(i.getReaderCardId().getId())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    private ReaderCardView toView(ReaderCard readerCard, List<ReceivedBook> receivedBooksByReaderCardId) {
        return ReaderCardView.builder()
                .id(readerCard.getId())
                .lastName(readerCard.getLastName())
                .firstName(readerCard.getFirstName())
                .middleName(readerCard.getMiddleName())
                .birthDate(readerCard.getBirthDate())
                .gender(readerCard.getGender())
                .insuranceNumber(readerCard.getInsuranceNumber())
                .receivedBooks(toView(receivedBooksByReaderCardId))
                .build();
    }
}
