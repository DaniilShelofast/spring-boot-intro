package mate.academy.project.repository.spec;

import mate.academy.project.model.Book;
import mate.academy.project.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> root.get("author")
                .in(param);
    }
}
