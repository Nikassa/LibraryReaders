package ru.my.task.libraryreaders.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.my.task.libraryreaders.controller.util.SwaggerDocuments;
import ru.my.task.libraryreaders.exceptions.ReaderCardException;
import ru.my.task.libraryreaders.exceptions.ReceivedBookException;
import ru.my.task.libraryreaders.model.ReceivedBookDTO;
import ru.my.task.libraryreaders.model.ReceivedBookView;
import ru.my.task.libraryreaders.service.ReceivedBookService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(tags = "Операции c получением книг")
public class ReceivedBookController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ReceivedBookService receivedBookService;

    @ApiOperation(value = "Получение книг по идентификатору", notes = SwaggerDocuments.GET_RECEIVED_BOOK_NOTES)
    @GetMapping(value = "/receivedBooks/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Long id) {
        logger.debug("Request to get receivedBook by id: {}", id);
        final ReceivedBookView receivedBook = receivedBookService.getById(id);

        return receivedBook != null
                ? new ResponseEntity<>(receivedBook, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation("Добавление получения книг")
    @PostMapping(value = "/receivedBooks")
    public ResponseEntity<?> create(@Valid @RequestBody ReceivedBookDTO dto) {
        logger.debug("Request to create receivedBook: {}", dto.toString());
        try {
            ReceivedBookView createdReceivedBook = receivedBookService.create(dto);
            logger.debug("Added receivedBook: {}", createdReceivedBook.toString());
            return new ResponseEntity<>(createdReceivedBook, HttpStatus.CREATED);
        } catch (ReaderCardException ex) {
            logger.debug("Cannot add receivedBook, id: {}", dto);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation("Редактирование получения книг")
    @PutMapping(value = "/receivedBooks/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @Valid @RequestBody ReceivedBookDTO dto) {
        logger.debug("Request to update receivedBook: {}", dto.toString());
        try {
            ReceivedBookView updatedReceivedBook = receivedBookService.update(dto, id);
            logger.debug("Updated receivedBook: {}", updatedReceivedBook.toString());
            return new ResponseEntity<>(updatedReceivedBook, HttpStatus.OK);
        } catch (ReaderCardException | ReceivedBookException ex) {
            logger.debug("Cannot update receivedBook, id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @ApiOperation("Удаление получения книг")
    @DeleteMapping(value = "/receivedBooks/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        logger.debug("Request to delete receivedBook by id: {}", id);
        try {
            receivedBookService.remove(id);
            logger.debug("Deleted receivedBook: with id: {}", id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ReceivedBookException ex) {
            logger.debug("Cannot delete receivedBook, id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}