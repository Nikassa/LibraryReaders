package ru.my.task.libraryreaders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.my.task.libraryreaders.exceptions.ReaderCardException;
import ru.my.task.libraryreaders.exceptions.ReceivedBookException;
import ru.my.task.libraryreaders.model.ReaderCard;
import ru.my.task.libraryreaders.model.ReceivedBook;
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
public class ReaderCardServiceImpl implements ReaderCardService {

    private final ReaderCardRepository readerCardRepository;
    private final ReceivedBookRepository receivedBookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReaderCardView> getAll() {
        List<ReaderCard> allReaderCards = readerCardRepository.findAll();
        return allReaderCards.stream()
                .map(i -> toView(i, Collections.EMPTY_LIST))
                .collect(Collectors.toList());
    }

    @Override
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

    @Override
    public ReaderCardView create(ReaderCardDTO dto) {
        ReaderCard readerCardForSave = fromDto(dto);
        ReaderCard savedReaderCard = readerCardRepository.saveAndFlush(readerCardForSave);

        List<ReceivedBook> savedReceivedBooksList = new ArrayList<>();
        if (dto.getReceivedBooks() != null) {
            savedReceivedBooksList = dto.getReceivedBooks().stream()
                    .map(i -> fromDto(i, savedReaderCard))
                    .collect(Collectors.toList())
                    .stream()
                    .map(i -> receivedBookRepository.saveAndFlush(i))
                    .collect(Collectors.toList());
        }
        return toView(savedReaderCard, savedReceivedBooksList);
    }

    @Override
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
                ReceivedBook receivedBookForSave = fromDto(receivedBookDto, receivedBookFromRepository.getReaderCardId());
                receivedBookForSave.setId(receivedBookFromRepository.getId());
                ReceivedBook savedReceivedBook = receivedBookRepository.saveAndFlush(receivedBookForSave);
                savedReceivedBooksList.add(savedReceivedBook);
            }
        }

        ReaderCard readerCardForSave = fromDto(dto);
        readerCardForSave.setId(id);
        ReaderCard savedReaderCard = readerCardRepository.save(readerCardForSave);
        return toView(savedReaderCard, savedReceivedBooksList);
    }

    @Override
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

    private ReaderCard fromDto(ReaderCardDTO readerCardDTO) {
        return ReaderCard.builder()
                .lastName(readerCardDTO.getLastName())
                .firstName(readerCardDTO.getFirstName())
                .middleName(readerCardDTO.getMiddleName())
                .birthDate(readerCardDTO.getBirthDate())
                .gender(readerCardDTO.getGender())
                .insuranceNumber(readerCardDTO.getInsuranceNumber())
                .build();
    }

    private ReceivedBook fromDto(ReceivedBookDTO receivedBookDTO, ReaderCard readerCardFromRepository) {
        return ReceivedBook.builder()
                .bookName(receivedBookDTO.getBookName())
                .returned(receivedBookDTO.getReturned())
                .dateBookReceived(receivedBookDTO.getDateBookReceived())
                .readerCardId(readerCardFromRepository)
                .build();
    }
}
