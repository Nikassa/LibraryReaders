package ru.my.task.libraryreaders.service;

import ru.my.task.libraryreaders.service.dto.ReceivedBookDTO;
import ru.my.task.libraryreaders.service.view.ReceivedBookView;

public interface ReceivedBookService {

    ReceivedBookView getById(Long id);

    ReceivedBookView create(ReceivedBookDTO dto);

    ReceivedBookView update(ReceivedBookDTO dto, Long id);

    boolean remove(Long id);
}