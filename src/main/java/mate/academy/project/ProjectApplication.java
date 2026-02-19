package mate.academy.project;

import mate.academy.project.model.Book;
import mate.academy.project.service.BookService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class ProjectApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("Hobbit");
                book.setAuthor("J. R. R. Tolkien");
                book.setIsbn("424534537");
                book.setPrice(BigDecimal.valueOf(120));
                book.setDescription("adventure");
                book.setCoverImage("yes");
                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }
}
