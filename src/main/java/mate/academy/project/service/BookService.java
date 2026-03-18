package mate.academy.project.service;

import java.util.List;
import mate.academy.project.dto.BookDto;
import mate.academy.project.dto.BookSearchParametersDto;
import mate.academy.project.dto.CreateBookRequestDto;
import mate.academy.project.dto.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateBookById(Long id, UpdateBookRequestDto updateBookRequestDto);

    List<BookDto> searchParameters(BookSearchParametersDto bookSearchParametersDto);
}
