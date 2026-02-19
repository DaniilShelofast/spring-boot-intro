package mate.academy.project.service;

import mate.academy.project.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);
    List<Book> findAll();
}
