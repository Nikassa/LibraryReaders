package ru.my.task.libraryreaders.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.my.task.libraryreaders.controller.util.SwaggerDocuments;
import ru.my.task.libraryreaders.exceptions.ReaderCardException;
import ru.my.task.libraryreaders.exceptions.ReceivedBookException;
import ru.my.task.libraryreaders.service.ReceivedBookService;
import ru.my.task.libraryreaders.service.dto.ReceivedBookDTO;
import ru.my.task.libraryreaders.service.view.ReceivedBookView;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(tags = "Операции c получением книг")
@Slf4j
public class ReceivedBookController {

    private final ReceivedBookService receivedBookService;

    @ApiOperation(value = "Получение книг по идентификатору", notes = SwaggerDocuments.GET_RECEIVED_BOOK_NOTES)
    @GetMapping(value = "/receivedBooks/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Long id) {
        log.debug("Request to get receivedBook by id: {}", id);
        final ReceivedBookView receivedBook = receivedBookService.getById(id);

        return receivedBook != null
                ? new ResponseEntity<>(receivedBook, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation("Добавление получения книг")
    @PostMapping(value = "/receivedBooks")
    public ResponseEntity<?> create(@Valid @RequestBody ReceivedBookDTO dto) {
        log.debug("Request to create receivedBook: {}", dto.toString());
        try {
            ReceivedBookView createdReceivedBook = receivedBookService.create(dto);
            log.debug("Added receivedBook: {}", createdReceivedBook.toString());
            return new ResponseEntity<>(createdReceivedBook, HttpStatus.CREATED);
        } catch (ReaderCardException ex) {
            log.debug("Cannot add receivedBook, id: {}", dto);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation("Редактирование получения книг")
    @PutMapping(value = "/receivedBooks/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @Valid @RequestBody ReceivedBookDTO dto) {
        log.debug("Request to update receivedBook: {}", dto.toString());
        try {
            ReceivedBookView updatedReceivedBook = receivedBookService.update(dto, id);
            log.debug("Updated receivedBook: {}", updatedReceivedBook.toString());
            return new ResponseEntity<>(updatedReceivedBook, HttpStatus.OK);
        } catch (ReaderCardException | ReceivedBookException ex) {
            log.debug("Cannot update receivedBook, id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @ApiOperation("Удаление получения книг")
    @DeleteMapping(value = "/receivedBooks/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        log.debug("Request to delete receivedBook by id: {}", id);
        try {
            receivedBookService.remove(id);
            log.debug("Deleted receivedBook: with id: {}", id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ReceivedBookException ex) {
            log.debug("Cannot delete receivedBook, id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}