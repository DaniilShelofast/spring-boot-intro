package mate.academy.project.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.project.dto.BookSearchParametersDto;
import mate.academy.project.model.Book;
import mate.academy.project.repository.SpecificationBuilder;
import mate.academy.project.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParameters) {
        Specification<Book> specification = Specification.where((Specification<Book>) null);

        if (bookSearchParameters.title() != null) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(bookSearchParameters.title()));
        }
        if (bookSearchParameters.title() != null) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(bookSearchParameters.title()));
        }
        return specification;
    }
}
