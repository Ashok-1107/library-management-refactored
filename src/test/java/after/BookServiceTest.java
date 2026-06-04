package after;

import after.model.Book;
import after.repository.InMemoryBookRepository;
import after.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BookService Tests")
class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(new InMemoryBookRepository());
    }

    @Test
    @DisplayName("Should add a book and find it by ID")
    void testAddAndFindBook() {
        bookService.addBook("B001", "Clean Code", "Robert Martin");

        Optional<Book> result = bookService.findBook("B001");

        assertTrue(result.isPresent());
        assertEquals("Clean Code", result.get().getTitle());
        assertEquals("Robert Martin", result.get().getAuthor());
    }

    @Test
    @DisplayName("New book should be available by default")
    void testNewBookIsAvailable() {
        bookService.addBook("B001", "Clean Code", "Robert Martin");

        Optional<Book> result = bookService.findBook("B001");

        assertTrue(result.isPresent());
        assertTrue(result.get().isAvailable());
    }

    @Test
    @DisplayName("Should return empty when book not found")
    void testFindNonExistentBook() {
        Optional<Book> result = bookService.findBook("INVALID");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should remove a book")
    void testRemoveBook() {
        bookService.addBook("B001", "Clean Code", "Robert Martin");
        bookService.removeBook("B001");

        Optional<Book> result = bookService.findBook("B001");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return only available books")
    void testGetAvailableBooks() {
        bookService.addBook("B001", "Clean Code", "Robert Martin");
        bookService.addBook("B002", "Refactoring", "Martin Fowler");

        // Mark one as borrowed
        bookService.findBook("B001").ifPresent(Book::markBorrowed);

        List<Book> available = bookService.getAvailableBooks();
        assertEquals(1, available.size());
        assertEquals("B002", available.get(0).getId());
    }

    @Test
    @DisplayName("Should return all books")
    void testGetAllBooks() {
        bookService.addBook("B001", "Clean Code", "Robert Martin");
        bookService.addBook("B002", "Refactoring", "Martin Fowler");

        assertEquals(2, bookService.getAllBooks().size());
    }
}
