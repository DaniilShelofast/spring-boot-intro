package mate.academy.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.project.dto.category.CreateCategoryDto;
import mate.academy.project.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
@AutoConfigureMockMvc
public class CategoryControllerTest {
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
    private CategoryRepository categoryRepository;

    private static final Long CATEGORY_ID = 1L;
    private static final String FIRST_PARAM_NAME = "Fiction";
    private static final String SECOND_PARAM_NAME = "History";
    private static final String FIRST_PARAM_DESCRIPTION =
            "Classic and modern fictional literature";
    private static final String SECOND_PARAM_DESCRIPTION = "Historical literature";

    @Test
    @DisplayName("Verify createCategory() method works")
    @Sql(scripts = "classpath:database/categories/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidDto_ReturnsCreatedDto() throws Exception {
        CreateCategoryDto requestDto = new CreateCategoryDto()
                .setName(FIRST_PARAM_NAME)
                .setDescription(FIRST_PARAM_DESCRIPTION);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(FIRST_PARAM_NAME))
                .andExpect(jsonPath("$.description").value(FIRST_PARAM_DESCRIPTION));
    }

    @Test
    @DisplayName("Verify getAll() method works")
    @Sql(scripts = "classpath:database/categories/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

    @Sql(scripts = "classpath:database/categories/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ValidPageable_ReturnsDtoPage() throws Exception {
        mockMvc.perform(
                        get("/categories?page=0&size=10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("user").roles("USER"))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.content[0].name").value(FIRST_PARAM_NAME))
                .andExpect(jsonPath("$.content[0].description").value(FIRST_PARAM_DESCRIPTION));
    }

    @Test
    @DisplayName("Verify getCategoryById() method works")
    @Sql(scripts = "classpath:database/categories/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

    @Sql(scripts = "classpath:database/categories/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ValidId_ReturnsDto() throws Exception {
        mockMvc.perform(
                        get("/categories/" + CATEGORY_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("user").roles("USER"))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(FIRST_PARAM_NAME));
    }

    @Test
    @DisplayName("Verify updateCategory() method works")
    @Sql(scripts = "classpath:database/categories/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

    @Sql(scripts = "classpath:database/categories/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidIdAndDto_ReturnsUpdatedDto() throws Exception {
        CreateCategoryDto request = new CreateCategoryDto()
                .setName(SECOND_PARAM_NAME)
                .setDescription(SECOND_PARAM_DESCRIPTION);
        String jsonRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/categories/" + CATEGORY_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(SECOND_PARAM_NAME));
    }

    @Test
    @DisplayName("Verify deleteCategory() method works")
    @Sql(scripts = "classpath:database/categories/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

    @Sql(scripts = "classpath:database/categories/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_ValidId_ExecutesSuccessfully() throws Exception {
        mockMvc.perform(
                        delete("/categories/" + CATEGORY_ID)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
        Assertions.assertFalse(categoryRepository.findById(CATEGORY_ID).isPresent(),
                "Category should be deleted from the database" + CATEGORY_ID);
    }
}
