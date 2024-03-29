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
import ru.my.task.libraryreaders.model.ReaderCardDTO;
import ru.my.task.libraryreaders.model.ReaderCardView;
import ru.my.task.libraryreaders.service.ReaderCardService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(tags = "Операции c картой читателя")
public class ReaderCardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ReaderCardService readerCardService;

    @ApiOperation(value = "Получение списка карт читателей", notes = SwaggerDocuments.GET_CARD_READER_NOTES)
    @GetMapping(value = "/readerCards", produces = {"application/json"})
    public ResponseEntity<List<ReaderCardView>> read() {
        logger.debug("Request to get readerCards list.");
        final List<ReaderCardView> readerCards = readerCardService.getAll();

        return readerCards != null && !readerCards.isEmpty()
                ? new ResponseEntity<>(readerCards, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Получение карты читателя по идентификатору", notes = SwaggerDocuments.GET_CARD_READER_NOTES)
    @GetMapping(value = "/readerCards/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Long id) {
        logger.debug("Request to get readerCard by id: {}", id);
        final ReaderCardView readerCardView = readerCardService.getById(id);

        return readerCardView != null
                ? new ResponseEntity<>(readerCardView, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation("Добавление карты читателя")
    @PostMapping(value = "/readerCards")
    public ResponseEntity<?> create(@Valid @RequestBody ReaderCardDTO dto) {
        logger.debug("Request to create readerCard: {} ", dto.toString());
        try {
            ReaderCardView createdReaderCard = readerCardService.create(dto);
            logger.debug("Added readerCard: {} ", createdReaderCard.toString());
            return new ResponseEntity<>(createdReaderCard, HttpStatus.CREATED);
        } catch (ReaderCardException ex) {
            logger.debug("Cannot add readerCard, id: {}", dto);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation("Редактирование карты читателя")
    @PutMapping(value = "/readerCards/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @Valid @RequestBody ReaderCardDTO dto) {
        logger.debug("Request to update readerCard: {}", dto.toString());
        try {
            ReaderCardView updatedReaderCard = readerCardService.update(dto, id);
            logger.debug("Updated readerCard: {}", updatedReaderCard.toString());
            return new ResponseEntity<>(updatedReaderCard, HttpStatus.OK);
        } catch (ReaderCardException | ReceivedBookException ex) {
            logger.debug("Cannot update readerCard, id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @ApiOperation("Удаление карты читателя")
    @DeleteMapping(value = "/readerCards/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        logger.debug("Request to delete readerCard by id: {}", id);
        try {
            readerCardService.remove(id);
            logger.debug("Deleted readerCard: with id: {}", id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ReceivedBookException ex) {
            logger.debug("Cannot delete readerCard, id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}