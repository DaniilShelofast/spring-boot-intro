package mate.academy.project.mapper;

import mate.academy.project.config.MapperConfig;
import mate.academy.project.dto.BookDto;
import mate.academy.project.dto.CreateBookRequestDto;
import mate.academy.project.dto.UpdateBookRequestDto;
import mate.academy.project.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBook(@MappingTarget Book book, UpdateBookRequestDto updateBookRequestDto);
}
