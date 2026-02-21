package mate.academy.project.service;

import java.util.List;
import mate.academy.project.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
