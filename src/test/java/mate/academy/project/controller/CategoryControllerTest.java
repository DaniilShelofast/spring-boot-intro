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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify createCategory() method works")
    void createCategory_ValidDto_ReturnsCreatedDto() throws Exception {
        CreateCategoryDto requestDto = new CreateCategoryDto()
                .setName("Fiction")
                .setDescription("Classic and modern fictional literature");

        CategoryDto extended = new CategoryDto()
                .setId(1L)
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());
        when(categoryService.save(requestDto)).thenReturn(extended);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Fiction"))
                .andExpect(jsonPath("$.description").value(
                        "Classic and modern fictional literature"));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify getAll() method works")
    void getAll_ValidPageable_ReturnsDtoPage() throws Exception {
        List<CategoryDto> categories = List.of(
                new CategoryDto().setId(1L).setName("Fiction")
                        .setDescription("Classic and modern fictional literature"),
                new CategoryDto().setId(2L).setName("History")
                        .setDescription("Historical literature")
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryDto> categoryPage = new PageImpl<>(categories, pageable, categories.size());
        when(categoryService.findAll(pageable)).thenReturn(categoryPage);
        mockMvc.perform(
                        get("/categories")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))

                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Fiction"))
                .andExpect(jsonPath("$.content[0].description")
                        .value("Classic and modern fictional literature"))

                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("History"))
                .andExpect(jsonPath("$.content[1].description").value("Historical literature"));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify getCategoryById() method works")
    void getCategoryById_ValidId_ReturnsDto() throws Exception {
        Long categoryId = 1L;
        CategoryDto expected = new CategoryDto()
                .setId(categoryId)
                .setName("Fiction");
        when(categoryService.getById(categoryId)).thenReturn(expected);
        mockMvc.perform(
                        get("/categories/" + categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Fiction"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify updateCategory() method works")
    void updateCategory_ValidIdAndDto_ReturnsUpdatedDto() throws Exception {
        Long categoryId = 1L;
        CreateCategoryDto request = new CreateCategoryDto()
                .setName("Adventure");
        CategoryDto categoryDto = new CategoryDto()
                .setId(categoryId)
                .setName(request.getName());
        when(categoryService.update(categoryId, request)).thenReturn(categoryDto);
        String jsonRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/categories/" + categoryId).content(jsonRequest).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Adventure"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify deleteCategory() method works")
    void deleteCategory_ValidId_ExecutesSuccessfully() throws Exception {
        Long categoryId = 1L;
        mockMvc.perform(
                        delete("/categories/" + categoryId)
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
        verify(categoryService, times(1)).deleteById(categoryId);
    }
}
