package mate.academy.project.repository;

import mate.academy.project.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/categories/add-category.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

@Sql(scripts = "classpath:database/categories/delete-category.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    private static final Long FICTION_CATEGORY_ID = 1L;
    @Test
    void findById_activeCategory_returnsCategory() {
        Optional<Category> actual = categoryRepository.findById(FICTION_CATEGORY_ID);
        assertTrue(actual.isPresent(), "Category with ID"
                + FICTION_CATEGORY_ID + " should exist in DB");
        assertEquals("Fiction", actual.get().getName());
    }
}
