package mate.academy.project.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.BookDto;
import mate.academy.project.dto.CreateBookRequestDto;
import mate.academy.project.exception.EntityNotFoundException;
import mate.academy.project.mapper.BookMapper;
import mate.academy.project.model.Book;
import mate.academy.project.repository.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public Optional<BookDto> getBookById(Long id) {
        Book book = bookRepository.getBookById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't get book by id" + id));
        return Optional.ofNullable(bookMapper.toDto(book));
    }
}
