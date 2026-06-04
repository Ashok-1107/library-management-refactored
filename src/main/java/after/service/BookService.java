package after.service;

import after.model.Book;
import after.repository.BookRepository;
import java.util.List;
import java.util.Optional;

/**
 * ✅ S - Only responsible for book management operations.
 * ✅ D - Depends on BookRepository abstraction, not a concrete class.
 */
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(String id, String title, String author) {
        Book book = new Book(id, title, author);
        bookRepository.save(book);
        System.out.println("Book added: " + title);
    }

    public void removeBook(String bookId) {
        bookRepository.delete(bookId);
    }

    public Optional<Book> findBook(String bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailable();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
