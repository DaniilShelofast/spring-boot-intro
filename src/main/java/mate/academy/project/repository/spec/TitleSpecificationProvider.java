package mate.academy.project.repository.spec;

import mate.academy.project.model.Book;
import mate.academy.project.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> root.get("title")
                .in(param);
    }
}
