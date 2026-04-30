package mate.academy.project.repository.pattern;

import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.book.BookSearchParametersDto;
import mate.academy.project.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParameters) {
        Specification<Book> specification = Specification.where((Specification<Book>) null);
        if (bookSearchParameters.title() != null) {
            specification = specification
                    .and(specificationProviderManager.getSpecificationProvider("title")
                            .getSpecification(bookSearchParameters.title()));
        }

        if (bookSearchParameters.author() != null) {
            specification = specification
                    .and(specificationProviderManager.getSpecificationProvider("author")
                            .getSpecification(bookSearchParameters.author()));
        }
        return specification;
    }
}
