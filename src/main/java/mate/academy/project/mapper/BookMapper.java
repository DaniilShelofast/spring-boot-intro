package mate.academy.project.mapper;

import java.util.List;
import mate.academy.project.config.MapperConfig;
import mate.academy.project.dto.book.BookDto;
import mate.academy.project.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.project.dto.book.CreateBookRequestDto;
import mate.academy.project.dto.book.UpdateBookRequestDto;
import mate.academy.project.model.Book;
import mate.academy.project.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "categories", ignore = true)
    void updateBook(@MappingTarget Book book, UpdateBookRequestDto updateBookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> categories = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoryIds(categories);
    }
}
