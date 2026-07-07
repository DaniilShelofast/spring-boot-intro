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

    @BeforeEach
    void setUp() {
        requestDto = new CreateBookRequestDto()
                .setTitle("Kobzar")
                .setAuthor("Taras Shevchenko")
                .setIsbn("978-966-03-8025-7")
                .setPrice(BigDecimal.valueOf(150))
                .setDescription("A collection of poetic works by Taras Shevchenko.")
                .setCoverImage("images/kobzar.jpg")
                .setCategoryIds(List.of(1L));

        category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Fiction literature");

        book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());
        book.setCategories(Set.of(category));

        bookDto = new BookDto()
                .setId(1L)
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoryIds(List.of(1L));
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
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    void findAll_ValidPageable_ReturnsDtoList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actual = bookService.findAll(pageable);
        assertThat(actual.getContent()).hasSize(1);
        assertThat(actual.getContent().get(0)).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify getById() method works")
    void findById_ExistingId_ReturnsDto() {
        Long bookId = 1L;
        book.setId(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.getBookById(bookId);
        assertThat(actual).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify update() method works")
    void update_ValidIdAndDto_ReturnsUpdatedDto() {
        Long bookId = 1L;
        book.setId(bookId);
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto();
        updateBookRequestDto.setTitle("Test");
        updateBookRequestDto.setPrice(BigDecimal.valueOf(200.00));

        book.setTitle(updateBookRequestDto.getTitle());
        book.setPrice(updateBookRequestDto.getPrice());

        bookDto.setTitle(book.getTitle());
        bookDto.setPrice(book.getPrice());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.updateBookById(bookId, updateBookRequestDto);
        assertThat(actual).isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Verify deleteById() method works")
    void deleteById_ValidId_ExecutesSuccessfully() {
        Long bookId = 1L;
        bookService.deleteById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
