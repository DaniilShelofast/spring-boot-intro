package mate.academy.project.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.project.dto.book.BookDto;
import mate.academy.project.dto.book.CreateBookRequestDto;
import mate.academy.project.dto.book.UpdateBookRequestDto;
import mate.academy.project.mapper.BookMapper;
import mate.academy.project.model.Book;
import mate.academy.project.model.Category;
import mate.academy.project.repository.BookRepository;
import mate.academy.project.repository.CategoryRepository;
import mate.academy.project.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookRequestDto requestDto;
    private Book book;
    private BookDto bookDto;
    private Category category;

    private static final Long BOOK_ID = 1L;
    private static final Long CATEGORY_ID = 1L;
    private static final String BOOK_TITLE = "Kobzar";
    private static final String BOOK_AUTHOR = "Taras Shevchenko";
    private static final String BOOK_ISBN = "978-966-03-8025-7";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(150);
    private static final String BOOK_DESC
            = "A collection of poetic works by Taras Shevchenko.";
    private static final String BOOK_IMAGE = "images/kobzar.jpg";
    private static final String CATEGORY_NAME_FICTION = "Fiction";
    private static final String CATEGORY_DESC_FICTION = "Fiction literature";
    private static final String UPDATE_TITLE = "Test";
    private static final BigDecimal UPDATE_PRICE = BigDecimal.valueOf(200.00);
    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;
    private static final Integer TEN = 10;

    @BeforeEach
    void setUp() {
        requestDto = new CreateBookRequestDto()
                .setTitle(BOOK_TITLE)
                .setAuthor(BOOK_AUTHOR)
                .setIsbn(BOOK_ISBN)
                .setPrice(BOOK_PRICE)
                .setDescription(BOOK_DESC)
                .setCoverImage(BOOK_IMAGE)
                .setCategoryIds(List.of(CATEGORY_ID));

        category = new Category();
        category.setId(BOOK_ID);
        category.setName(CATEGORY_NAME_FICTION);
        category.setDescription(CATEGORY_DESC_FICTION);

        book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());
        book.setCategories(Set.of(category));

        bookDto = new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoryIds(List.of(CATEGORY_ID));
    }

    @Test
    @DisplayName("Verify save()  method works")
    void save_WithValidCreateBookRequestDto_ReturnsBookDto() {
        when(categoryRepository.findAllById(requestDto.getCategoryIds()))
                .thenReturn(List.of(category));
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.save(requestDto);
        assertThat(actual).isEqualTo(bookDto);
        verify(bookRepository, times(ONE)).save(book);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    void findAll_ValidPageable_ReturnsDtoList() {
        Pageable pageable = PageRequest.of(ZERO, TEN);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actual = bookService.findAll(pageable);
        assertThat(actual.getContent()).hasSize(ONE);
        assertThat(actual.getContent().get(ZERO)).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify getById() method works")
    void findById_ExistingId_ReturnsDto() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.getBookById(BOOK_ID);
        assertThat(actual).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify update() method works")
    void update_ValidIdAndDto_ReturnsUpdatedDto() {
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto();
        updateBookRequestDto.setTitle(UPDATE_TITLE);
        updateBookRequestDto.setPrice(UPDATE_PRICE);

        book.setId(BOOK_ID);
        book.setTitle(updateBookRequestDto.getTitle());
        book.setPrice(updateBookRequestDto.getPrice());

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setPrice(book.getPrice());

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.updateBookById(BOOK_ID, updateBookRequestDto);
        assertThat(actual).isEqualTo(bookDto);
        verify(bookRepository, times(ONE)).save(book);
    }

    @Test
    @DisplayName("Verify deleteById() method works")
    void deleteById_ValidId_ExecutesSuccessfully() {
        bookService.deleteById(BOOK_ID);
        verify(bookRepository, times(ONE)).deleteById(BOOK_ID);
    }
}
