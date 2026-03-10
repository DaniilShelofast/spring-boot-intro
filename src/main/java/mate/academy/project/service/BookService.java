package mate.academy.project.service;

import java.util.List;
import mate.academy.project.dto.BookDto;
import mate.academy.project.dto.CreateBookRequestDto;
import mate.academy.project.dto.UpdateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateBookById(Long id, UpdateBookRequestDto updateBookRequestDto);
}
