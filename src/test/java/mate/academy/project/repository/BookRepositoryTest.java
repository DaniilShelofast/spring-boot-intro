package mate.academy.project.repository;

import mate.academy.project.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;
    private static final Long FICTION_CATEGORY_ID = 1L;
    private static final Long FICTION_BOOK_ID = 1L;

    @Test
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
    void findAllByCategoryId_WhenBooksExist_ReturnsBooks() {
        List<Book> books = bookRepository.findAllByCategoryId(FICTION_CATEGORY_ID);
        assertFalse(books.isEmpty(), "The list of books should not be empty!");
        assertEquals(ONE, books.size());
        assertEquals("Kobzar", books.get(ZERO).getTitle());
    }

    @Test
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
    void findById_activeBook_returnsBook() {
        Optional<Book> actual = bookRepository.findById(FICTION_BOOK_ID);
        assertTrue(actual.isPresent(), "Book with ID " + FICTION_BOOK_ID + " should exist in DB");
        assertEquals("Kobzar", actual.get().getTitle());
    }
}
