package mate.academy.project.service;

import mate.academy.project.dto.category.CategoryDto;
import mate.academy.project.dto.category.CreateCategoryDto;
import mate.academy.project.mapper.CategoryMapper;
import mate.academy.project.model.Category;
import mate.academy.project.repository.CategoryRepository;
import mate.academy.project.service.impl.CategoryServiceImpl;
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
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CreateCategoryDto createCategoryDto;
    private CategoryDto categoryDto;
    private Category category;

    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;
    private static final Integer TEN = 10;
    private static final Long FICTION_CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME_FICTION = "Fiction";
    private static final String CATEGORY_DESC_FICTION = "Fiction literature";
    private static final String CATEGORY_NAME_UPDATE = "Test";
    private static final String CATEGORY_DESC_UPDATE = "Test description";

    @BeforeEach
    void setUp() {
        createCategoryDto = new CreateCategoryDto()
                .setName(CATEGORY_NAME_FICTION)
                .setDescription(CATEGORY_DESC_FICTION);

        category = new Category();
        category.setId(FICTION_CATEGORY_ID);
        category.setName(createCategoryDto.getName());
        category.setDescription(createCategoryDto.getDescription());

        categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
    }

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidDto_ReturnsDto() {
        when(categoryMapper.toEntity(createCategoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.save(createCategoryDto);
        assertThat(actual).isEqualTo(categoryDto);
        verify(categoryRepository, times(ONE)).save(category);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    void findAll_ValidPageable_ReturnsDtoPage() {
        Pageable pageable = PageRequest.of(ZERO, TEN);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> actual = categoryService.findAll(pageable);
        assertThat(actual.getContent()).hasSize(ONE);
        assertThat(actual.getContent().get(ZERO)).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Verify getById() method works")
    void getById_ValidId_ReturnsDto() {
        when(categoryRepository.findById(FICTION_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.getById(FICTION_CATEGORY_ID);
        assertThat(actual).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Verify update() method works")
    void update_ValidIdAndDto_ReturnsUpdatedDto() {
        CreateCategoryDto updateDto = new CreateCategoryDto()
                .setName(CATEGORY_NAME_UPDATE)
                .setDescription(CATEGORY_DESC_UPDATE);

        category.setName(updateDto.getName());
        category.setDescription(updateDto.getDescription());

        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryRepository.findById(FICTION_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.update(FICTION_CATEGORY_ID, updateDto);
        assertThat(actual).isEqualTo(categoryDto);
        verify(categoryRepository, times(ONE)).save(category);
    }

    @Test
    @DisplayName("Verify deleteById() method works")
    void deleteById_ValidId_ExecutesSuccessfully() {
        categoryService.deleteById(FICTION_CATEGORY_ID);
        verify(categoryRepository, times(ONE)).deleteById(FICTION_CATEGORY_ID);
    }
}
