package mate.academy.project.service;

import java.util.List;
import mate.academy.project.dto.book.BookDto;
import mate.academy.project.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.project.dto.book.BookSearchParametersDto;
import mate.academy.project.dto.book.CreateBookRequestDto;
import mate.academy.project.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDtoWithoutCategoryIds> findAll(Pageable pageable);

    BookDtoWithoutCategoryIds getBookById(Long id);

    void deleteById(Long id);

    BookDto updateBookById(Long id, UpdateBookRequestDto updateBookRequestDto);

    List<BookDto> searchParameters(BookSearchParametersDto bookSearchParametersDto);

    List<BookDto> findAllByCategoryId(Long categoryId);
}
