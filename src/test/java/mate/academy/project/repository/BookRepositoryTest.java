package mate.academy.project.repository;

import mate.academy.project.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    void findAllByCategoryId_WhenBooksExist_ReturnsBooks() {
        Long targetCategoryId = 1L;

        List<Book> books = bookRepository.findAllByCategoryId(targetCategoryId);
        assertFalse(books.isEmpty(), "The list of books should not be empty!");
        assertEquals(1, books.size());
        assertEquals("Kobzar", books.get(0).getTitle());
    }

    @Test
    void findById_activeBook_returnsBook() {
        Optional<Book> actual = bookRepository.findById(1L);
        assertTrue(actual.isPresent(), "Book with ID 1L should exist in DB");
        assertEquals("Kobzar", actual.get().getTitle());
    }
}
