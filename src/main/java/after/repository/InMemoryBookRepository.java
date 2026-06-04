package after.repository;

import after.model.Book;
import java.util.*;

/**
 * ✅ S - Only responsible for storing and retrieving books.
 * ✅ D - Implements BookRepository so it can be swapped for a DB-backed version.
 */
public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> store = new HashMap<>();

    @Override
    public void save(Book book) {
        store.put(book.getId(), book);
    }

    @Override
    public Optional<Book> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Book> findAvailable() {
        List<Book> available = new ArrayList<>();
        for (Book b : store.values()) {
            if (b.isAvailable()) available.add(b);
        }
        return available;
    }

    @Override
    public void delete(String id) {
        store.remove(id);
    }
}
