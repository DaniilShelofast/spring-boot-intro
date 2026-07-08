package mate.academy.project.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.academy.project.dto.book.BookDto;
import mate.academy.project.dto.book.CreateBookRequestDto;
import mate.academy.project.dto.book.UpdateBookRequestDto;
import mate.academy.project.security.JwtUtil;
import mate.academy.project.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;
    private static final Integer SIZE = 2;
    private static final Integer TEN = 10;
    private static final Long FIRST_BOOK_ID = 1L;
    private static final Long SECOND_BOOK_ID = 2L;
    private static final Long FIRST_CATEGORY_ID = 1L;
    private static final String FIRST_TITLE_NAME = "Kobzar";
    private static final String UPDATE_TITLE_NAME = "New Kobzar";
    private static final String SECOND_TITLE_NAME = "Eneida";
    private static final String SECOND_AUTHOR_NAME = "Ivan Kotliarevskyi";

    @Test
    @DisplayName("Verify createBook() method works")
    void createBook_ValidDto_ReturnsCreatedDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle(SECOND_TITLE_NAME)
                .setAuthor(SECOND_AUTHOR_NAME)
                .setCategoryIds(List.of(FIRST_CATEGORY_ID));

        BookDto expected = new BookDto()
                .setId(SECOND_BOOK_ID)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setCategoryIds(requestDto.getCategoryIds());

        when(bookService.save(requestDto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(SECOND_BOOK_ID))
                .andExpect(jsonPath("$.title").value(SECOND_TITLE_NAME))
                .andExpect(jsonPath("$.author").value(SECOND_AUTHOR_NAME))
                .andExpect(jsonPath("$.categoryIds[0]").value(FIRST_CATEGORY_ID));
    }

    @Test
    @DisplayName("Verify getAll() method works")
    void getAll_ValidPageable_ReturnsDtoPage() throws Exception {
        List<BookDto> books = List.of(
                new BookDto().setId(FIRST_BOOK_ID).setTitle(FIRST_TITLE_NAME),
                new BookDto().setId(SECOND_BOOK_ID).setTitle(SECOND_TITLE_NAME)
        );
        Pageable pageable = PageRequest.of(ZERO, TEN);
        Page<BookDto> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookService.findAll(pageable)).thenReturn(bookPage);
        mockMvc.perform(
                        get("/books")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("user").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(SIZE)))
                .andExpect(jsonPath("$.content[0].id").value(FIRST_BOOK_ID))
                .andExpect(jsonPath("$.content[0].title").value(FIRST_TITLE_NAME))
                .andExpect(jsonPath("$.content[1].id").value(SECOND_BOOK_ID))
                .andExpect(jsonPath("$.content[1].title").value(SECOND_TITLE_NAME));
    }

    @Test
    @DisplayName("Verify getBookById() method works")
    void getBookById_ValidId_ReturnsDto() throws Exception {
        BookDto expected = new BookDto()
                .setId(FIRST_BOOK_ID)
                .setTitle(FIRST_TITLE_NAME);
        when(bookService.getBookById(FIRST_BOOK_ID)).thenReturn(expected);
        mockMvc.perform(
                        get("/books/" + FIRST_BOOK_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("user").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIRST_BOOK_ID))
                .andExpect(jsonPath("$.title").value(FIRST_TITLE_NAME));
    }

    @Test
    @DisplayName("Verify updateBook() method works")
    void updateBook_ValidIdAndDto_ReturnsUpdatedDto() throws Exception {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto()
                .setTitle(UPDATE_TITLE_NAME);
        BookDto expected = new BookDto()
                .setId(FIRST_BOOK_ID)
                .setTitle(requestDto.getTitle());
        when(bookService.updateBookById(FIRST_BOOK_ID, requestDto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        put("/books/" + FIRST_BOOK_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIRST_BOOK_ID))
                .andExpect(jsonPath("$.title").value(UPDATE_TITLE_NAME));
    }

    @Test
    @DisplayName("Verify deleteBook() method works")
    void deleteBook_ValidId_ExecutesSuccessfully() throws Exception {
        mockMvc.perform(
                        delete("/books/" + FIRST_BOOK_ID)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
        verify(bookService, times(ONE)).deleteById(FIRST_BOOK_ID);
    }
}
