package after.repository;

import after.model.Book;
import java.util.List;
import java.util.Optional;

/**
 * ✅ D - Service layer depends on this abstraction, not on a concrete list or DB.
 * ✅ I - Focused only on book persistence operations.
 */
public interface BookRepository {
    void save(Book book);
    Optional<Book> findById(String id);
    List<Book> findAll();
    List<Book> findAvailable();
    void delete(String id);
}
