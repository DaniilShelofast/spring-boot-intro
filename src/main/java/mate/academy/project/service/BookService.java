package mate.academy.project.service;

import java.util.List;
import java.util.Optional;
import mate.academy.project.dto.BookDto;
import mate.academy.project.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);
}
