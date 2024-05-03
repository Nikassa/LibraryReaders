package ru.my.task.libraryreaders.service;

import ru.my.task.libraryreaders.service.dto.ReaderCardDTO;
import ru.my.task.libraryreaders.service.view.ReaderCardView;

import java.util.List;

public interface ReaderCardService {

    List<ReaderCardView> getAll();

    ReaderCardView getById(Long id);

    ReaderCardView create(ReaderCardDTO dto);

    ReaderCardView update(ReaderCardDTO dto, Long id);

    boolean remove(Long id);
}
