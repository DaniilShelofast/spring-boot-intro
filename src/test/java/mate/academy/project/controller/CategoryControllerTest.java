package mate.academy.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.project.dto.category.CategoryDto;
import mate.academy.project.dto.category.CreateCategoryDto;
import mate.academy.project.security.JwtUtil;
import mate.academy.project.service.BookService;
import mate.academy.project.service.CategoryService;
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
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerTest {
    @MockitoBean
    private CategoryService categoryService;
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
    private static final Long FIRST_CATEGORY_ID = 1L;
    private static final Long SECOND_CATEGORY_ID = 2L;
    private static final String FIRST_PARAM_NAME = "Fiction";
    private static final String SECOND_PARAM_NAME = "History";
    private static final String PARAM_NAME_UPDATE = "Adventure";
    private static final String FIRST_PARAM_DESCRIPTION =
            "Classic and modern fictional literature";
    private static final String SECOND_PARAM_DESCRIPTION = "Historical literature";

    @Test
    @DisplayName("Verify createCategory() method works")
    void createCategory_ValidDto_ReturnsCreatedDto() throws Exception {
        CreateCategoryDto requestDto = new CreateCategoryDto()
                .setName(FIRST_PARAM_NAME)
                .setDescription(FIRST_PARAM_DESCRIPTION);

        CategoryDto extended = new CategoryDto()
                .setId(FIRST_CATEGORY_ID)
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());
        when(categoryService.save(requestDto)).thenReturn(extended);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(FIRST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(FIRST_PARAM_NAME))
                .andExpect(jsonPath("$.description").value(FIRST_PARAM_DESCRIPTION));
    }

    @Test
    @DisplayName("Verify getAll() method works")
    void getAll_ValidPageable_ReturnsDtoPage() throws Exception {
        List<CategoryDto> categories = List.of(
                new CategoryDto().setId(FIRST_CATEGORY_ID).setName(FIRST_PARAM_NAME)
                        .setDescription(FIRST_PARAM_DESCRIPTION),
                new CategoryDto().setId(SECOND_CATEGORY_ID).setName(SECOND_PARAM_NAME)
                        .setDescription(SECOND_PARAM_DESCRIPTION)
        );

        Pageable pageable = PageRequest.of(ZERO, TEN);
        Page<CategoryDto> categoryPage = new PageImpl<>(categories, pageable, categories.size());
        when(categoryService.findAll(pageable)).thenReturn(categoryPage);
        mockMvc.perform(
                        get("/categories")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("user").roles("USER"))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(SIZE)))

                .andExpect(jsonPath("$.content[0].id").value(FIRST_CATEGORY_ID))
                .andExpect(jsonPath("$.content[0].name").value(FIRST_PARAM_NAME))
                .andExpect(jsonPath("$.content[0].description").value(FIRST_PARAM_DESCRIPTION))

                .andExpect(jsonPath("$.content[1].id").value(SECOND_CATEGORY_ID))
                .andExpect(jsonPath("$.content[1].name").value(SECOND_PARAM_NAME))
                .andExpect(jsonPath("$.content[1].description").value(SECOND_PARAM_DESCRIPTION));
    }

    @Test
    @DisplayName("Verify getCategoryById() method works")
    void getCategoryById_ValidId_ReturnsDto() throws Exception {
        CategoryDto expected = new CategoryDto()
                .setId(FIRST_CATEGORY_ID)
                .setName(FIRST_PARAM_NAME);
        when(categoryService.getById(FIRST_CATEGORY_ID)).thenReturn(expected);
        mockMvc.perform(
                        get("/categories/" + FIRST_CATEGORY_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(user("user").roles("USER"))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIRST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(FIRST_PARAM_NAME));
    }

    @Test
    @DisplayName("Verify updateCategory() method works")
    void updateCategory_ValidIdAndDto_ReturnsUpdatedDto() throws Exception {
        CreateCategoryDto request = new CreateCategoryDto()
                .setName(PARAM_NAME_UPDATE);
        CategoryDto categoryDto = new CategoryDto()
                .setId(FIRST_CATEGORY_ID)
                .setName(request.getName());
        when(categoryService.update(FIRST_CATEGORY_ID, request)).thenReturn(categoryDto);
        String jsonRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/categories/" + FIRST_CATEGORY_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FIRST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(PARAM_NAME_UPDATE));
    }

    @Test
    @DisplayName("Verify deleteCategory() method works")
    void deleteCategory_ValidId_ExecutesSuccessfully() throws Exception {
        mockMvc.perform(
                        delete("/categories/" + FIRST_CATEGORY_ID)
                                .with(user("admin").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
        verify(categoryService, times(ONE)).deleteById(FIRST_CATEGORY_ID);
    }
}
