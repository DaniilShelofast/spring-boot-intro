package mate.academy.project.repository;

import java.util.List;
import mate.academy.project.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
