package mate.academy.project.mapper;

import mate.academy.project.config.MapperConfig;
import mate.academy.project.dto.category.CategoryDto;
import mate.academy.project.dto.category.CreateCategoryDto;
import mate.academy.project.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryDto categoryDto);

    void updateCategory(@MappingTarget Category category, CreateCategoryDto createCategoryDto);
}
