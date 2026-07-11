package mate.academy.project.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.project.dto.book.CreateBookRequestDto;
import mate.academy.project.dto.book.UpdateBookRequestDto;
import mate.academy.project.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
@AutoConfigureMockMvc
public class BookControllerTest {
    @Container
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    private final static Long BOOK_ID = 1L;

    @Test
    @DisplayName("Verify createBook() method works")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books-categories/delete-book-category.sql",
            "classpath:database/books/delete-book.sql",
            "classpath:database/categories/delete-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidDto_ReturnsCreatedDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Kobzar")
                .setAuthor("Taras Shevchenko")
                .setIsbn("978-966-03-8025-7")
                .setPrice(BigDecimal.valueOf(49.95))
                .setDescription("A collection of poetic works by Taras Shevchenko.")
                .setCoverImage("images/kobzar.jpg")
                .setCategoryIds(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(requestDto.getTitle()))
                .andExpect(jsonPath("$.author").value(requestDto.getAuthor()))
                .andExpect(jsonPath("$.isbn").value(requestDto.getIsbn()))
                .andExpect(jsonPath("$.price").value(requestDto.getPrice()))
                .andExpect(jsonPath("$.categoryIds[0]").value(1));
    }

    @Test
    @DisplayName("Verify getAll() method works")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql",
            "classpath:database/books/add-book.sql",
            "classpath:database/books-categories/add-book-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books-categories/delete-book-category.sql",
            "classpath:database/books/delete-book.sql",
            "classpath:database/categories/delete-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ValidPageable_ReturnsDtoPage() throws Exception {
        mockMvc.perform(
                        get("/books?page=0&size=10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("user").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Kobzar"))
                .andExpect(jsonPath("$.content[0].author").value("Taras Shevchenko"));
    }

    @Test
    @DisplayName("Verify getBookById() method works")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql",
            "classpath:database/books/add-book.sql",
            "classpath:database/books-categories/add-book-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books-categories/delete-book-category.sql",
            "classpath:database/books/delete-book.sql",
            "classpath:database/categories/delete-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_ValidId_ReturnsDto() throws Exception {
        mockMvc.perform(
                        get("/books/" + BOOK_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("user").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOK_ID))
                .andExpect(jsonPath("$.title").value("Kobzar"));
    }

    @Test
    @DisplayName("Verify updateBook() method works")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql",
            "classpath:database/books/add-book.sql",
            "classpath:database/books-categories/add-book-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books-categories/delete-book-category.sql",
            "classpath:database/books/delete-book.sql",
            "classpath:database/categories/delete-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_ValidIdAndDto_ReturnsUpdatedDto() throws Exception {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto()
                .setTitle("Deep Work")
                .setAuthor("Cal Newport")
                .setIsbn("978-0316336420")
                .setPrice(BigDecimal.valueOf(20))
                .setCategoryIds(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        put("/books/" + BOOK_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOK_ID))
                .andExpect(jsonPath("$.title").value("Deep Work"));
    }

    @Test
    @DisplayName("Verify deleteBook() method works")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql",
            "classpath:database/books/add-book.sql",
            "classpath:database/books-categories/add-book-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books-categories/delete-book-category.sql",
            "classpath:database/books/delete-book.sql",
            "classpath:database/categories/delete-category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_ValidId_ExecutesSuccessfully() throws Exception {
        mockMvc.perform(
                        delete("/books/" + BOOK_ID)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
        assertFalse(bookRepository.findById(BOOK_ID).isPresent(),
                "Book should be deleted from the database");
    }
}
