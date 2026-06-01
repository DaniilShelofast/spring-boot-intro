package mate.academy.project.service.impl;

import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.book.BookDto;
import mate.academy.project.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.project.dto.book.BookSearchParametersDto;
import mate.academy.project.dto.book.CreateBookRequestDto;
import mate.academy.project.dto.book.UpdateBookRequestDto;
import mate.academy.project.exception.EntityNotFoundException;
import mate.academy.project.mapper.BookMapper;
import mate.academy.project.model.Book;
import mate.academy.project.model.Category;
import mate.academy.project.repository.BookRepository;
import mate.academy.project.repository.CategoryRepository;
import mate.academy.project.repository.pattern.SpecificationBuilder;
import mate.academy.project.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationBuilder<Book> builder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        List<Category> categoriesByIds = categoryRepository
                .findAllById(requestDto.getCategoryIds());
        book.setCategories(new HashSet<>(categoriesByIds));
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't get book by id" + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBookById(Long id, UpdateBookRequestDto updateBookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't get book by id" + id));
        bookMapper.updateBook(book, updateBookRequestDto);
        List<Category> categoriesByIds = categoryRepository
                .findAllById(updateBookRequestDto.getCategoryIds());
        book.setCategories(new HashSet<>(categoriesByIds));
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> searchParameters(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> bookSpecification = builder.build(bookSearchParametersDto);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
