package mate.academy.project.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.security.test.context.support.WithMockUser;
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

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify createBook() method works")
    void createBook_ValidDto_ReturnsCreatedDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Eneida")
                .setAuthor("Ivan Kotliarevskyi")
                .setCategoryIds(List.of(1L));

        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setCategoryIds(requestDto.getCategoryIds());

        when(bookService.save(requestDto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Eneida"))
                .andExpect(jsonPath("$.author").value("Ivan Kotliarevskyi"))
                .andExpect(jsonPath("$.categoryIds[0]").value(1L));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify getAll() method works")
    void getAll_ValidPageable_ReturnsDtoPage() throws Exception {
        List<BookDto> books = List.of(
                new BookDto().setId(1L).setTitle("Kobzar"),
                new BookDto().setId(2L).setTitle("Eneida")
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDto> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookService.findAll(pageable)).thenReturn(bookPage);
        mockMvc.perform(
                        get("/books")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Kobzar"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].title").value("Eneida"));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify getBookById() method works")
    void getBookById_ValidId_ReturnsDto() throws Exception {
        Long bookId = 1L;
        BookDto expected = new BookDto()
                .setId(bookId)
                .setTitle("Kobzar");
        when(bookService.getBookById(bookId)).thenReturn(expected);
        mockMvc.perform(
                        get("/books/" + bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Kobzar"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify updateBook() method works")
    void updateBook_ValidIdAndDto_ReturnsUpdatedDto() throws Exception {
        Long bookId = 1L;
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto()
                .setTitle("New Kobzar 2026");
        BookDto expected = new BookDto()
                .setId(bookId)
                .setTitle(requestDto.getTitle());
        when(bookService.updateBookById(bookId, requestDto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        put("/books/" + bookId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("New Kobzar 2026"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify deleteBook() method works")
    void deleteBook_ValidId_ExecutesSuccessfully() throws Exception {
        Long bookId = 1L;
        mockMvc.perform(
                        delete("/books/" + bookId)
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteById(bookId);
    }
}
