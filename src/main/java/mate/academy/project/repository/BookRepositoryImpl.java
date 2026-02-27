package mate.academy.project.repository;

import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.project.exception.EntityNotFoundException;
import mate.academy.project.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert book " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Book getBookById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Book.class, id);
        } catch (RuntimeException e) {
            throw new EntityNotFoundException("Can't get book by id" + id);
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Book> criteriaQuery = session.getCriteriaBuilder()
                    .createQuery(Book.class);
            criteriaQuery.from(Book.class);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all books", e);
        }
    }
}
