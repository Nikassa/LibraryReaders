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
import ru.my.task.libraryreaders.service.dto.ReceivedBookDTO;
import ru.my.task.libraryreaders.service.view.ReceivedBookView;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReceivedBookService {

    private final ReceivedBookRepository receivedBookRepository;
    private final ReaderCardRepository readerCardRepository;

    @Transactional(readOnly = true)
    public ReceivedBookView getById(Long id) {
        Optional<ReceivedBook> receivedBookFromRepository = receivedBookRepository.findById(id);
        if (receivedBookFromRepository.isPresent()) {
            return toView(receivedBookFromRepository.get());
        } else {
            return null;
        }
    }

    public ReceivedBookView create(ReceivedBookDTO dto) {
        Optional<ReaderCard> readerCardFromRepository = readerCardRepository.findById(dto.getReaderCardId());
        if (!readerCardFromRepository.isPresent()) {
            throw new ReaderCardException("Cannot find ReaderCard with id: ", dto.getReaderCardId());
        }
        ReceivedBook receivedBookForSave = fromDto(dto, readerCardFromRepository.get());
        ReceivedBook savedReceivedBook = receivedBookRepository.save(receivedBookForSave);
        return toView(savedReceivedBook);
    }

    public ReceivedBookView update(ReceivedBookDTO dto, Long id) {
        Optional<ReceivedBook> receivedBookFromRepository = receivedBookRepository.findById(id);
        if (!receivedBookFromRepository.isPresent()) {
            throw new ReceivedBookException("Cannot find ReceivedBook with id: ", id);
        }
        Optional<ReaderCard> readerCardFromRepository = readerCardRepository.findById(dto.getReaderCardId());
        if (!readerCardFromRepository.isPresent()) {
            throw new ReaderCardException("Cannot find ReaderCard with id: ", dto.getReaderCardId());
        }
        ReceivedBook receivedBookForSave = fromDto(dto, readerCardFromRepository.get());
        receivedBookForSave.setId(id);
        ReceivedBook savedReceivedBook = receivedBookRepository.save(receivedBookForSave);
        return toView(savedReceivedBook);
    }

    public boolean remove(Long id) {
        Optional<ReceivedBook> receivedBookFromRepository = receivedBookRepository.findById(id);
        if (receivedBookFromRepository.isPresent()) {
            receivedBookRepository.delete(receivedBookFromRepository.get());
            return true;
        } else {
            throw new ReceivedBookException("Cannot remove ReceivedBook with id: ", id);
        }
    }

    private ReceivedBookView toView(ReceivedBook readerCardReceivedBooks) {
        return ReceivedBookView.builder()
                .id(readerCardReceivedBooks.getId())
                .bookName(readerCardReceivedBooks.getBookName())
                .returned(readerCardReceivedBooks.getReturned())
                .dateBookReceived(readerCardReceivedBooks.getDateBookReceived())
                .readerCardId(readerCardReceivedBooks.getReaderCardId().getId())
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