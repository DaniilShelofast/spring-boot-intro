package mate.academy.project.repository;

import mate.academy.project.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);
    List<Book> findAll();
}
