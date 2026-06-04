package after;

import after.model.*;
import after.notification.NotificationService;
import after.repository.InMemoryBookRepository;
import after.repository.InMemoryMemberRepository;
import after.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BorrowService Tests")
class BorrowServiceTest {

    private BorrowService            borrowService;
    private InMemoryBookRepository   bookRepo;
    private InMemoryMemberRepository memberRepo;

    private static class FakeNotifier implements NotificationService {
        @Override
        public void sendNotification(String r, String s, String m) {}
    }

    @BeforeEach
    void setUp() {
        bookRepo      = new InMemoryBookRepository();
        memberRepo    = new InMemoryMemberRepository();
        borrowService = new BorrowService(bookRepo, memberRepo, new FakeNotifier());

        // Seed data
        bookRepo.save(new Book("B001", "Clean Code", "Robert Martin"));
        bookRepo.save(new Book("B002", "Refactoring", "Martin Fowler"));
        bookRepo.save(new Book("B003", "Design Patterns", "GoF"));
        bookRepo.save(new Book("B004", "SOLID Principles", "Robert Martin"));

        memberRepo.save(new StandardMember("M001", "Ashok",  "ashok@example.com"));
        memberRepo.save(new PremiumMember ("M002", "Raj",    "raj@example.com"));
        memberRepo.save(new StudentMember ("M003", "Priya",  "priya@example.com"));
    }

    @Test
    @DisplayName("Should successfully borrow an available book")
    void testBorrowBookSuccess() {
        boolean result = borrowService.borrowBook("B001", "M001");
        assertTrue(result);
    }

    @Test
    @DisplayName("Book should be marked unavailable after borrowing")
    void testBookMarkedUnavailableAfterBorrow() {
        borrowService.borrowBook("B001", "M001");
        assertFalse(bookRepo.findById("B001").get().isAvailable());
    }

    @Test
    @DisplayName("Should not borrow an already borrowed book")
    void testCannotBorrowUnavailableBook() {
        borrowService.borrowBook("B001", "M001");
        boolean secondBorrow = borrowService.borrowBook("B001", "M002");
        assertFalse(secondBorrow);
    }

    @Test
    @DisplayName("Standard member cannot exceed borrow limit of 3")
    void testStandardMemberBorrowLimit() {
        borrowService.borrowBook("B001", "M001");
        borrowService.borrowBook("B002", "M001");
        borrowService.borrowBook("B003", "M001");

        boolean fourthBorrow = borrowService.borrowBook("B004", "M001");
        assertFalse(fourthBorrow);
        assertEquals(3, borrowService.countBorrowedByMember("M001"));
    }

    @Test
    @DisplayName("Student member cannot exceed borrow limit of 2")
    void testStudentMemberBorrowLimit() {
        borrowService.borrowBook("B001", "M003");
        borrowService.borrowBook("B002", "M003");

        boolean thirdBorrow = borrowService.borrowBook("B003", "M003");
        assertFalse(thirdBorrow);
        assertEquals(2, borrowService.countBorrowedByMember("M003"));
    }

    @Test
    @DisplayName("Premium member can borrow up to 10 books")
    void testPremiumMemberHigherLimit() {
        // Premium limit is 10, we only have 4 books seeded
        borrowService.borrowBook("B001", "M002");
        borrowService.borrowBook("B002", "M002");
        borrowService.borrowBook("B003", "M002");
        borrowService.borrowBook("B004", "M002");

        assertEquals(4, borrowService.countBorrowedByMember("M002"));
    }

    @Test
    @DisplayName("Should return a borrowed book successfully")
    void testReturnBook() {
        borrowService.borrowBook("B001", "M001");
        boolean returned = borrowService.returnBook("B001", "M001");

        assertTrue(returned);
        assertTrue(bookRepo.findById("B001").get().isAvailable());
    }

    @Test
    @DisplayName("Can borrow same book again after it is returned")
    void testBorrowAfterReturn() {
        borrowService.borrowBook("B001", "M001");
        borrowService.returnBook("B001", "M001");

        boolean reBorrow = borrowService.borrowBook("B001", "M002");
        assertTrue(reBorrow);
    }

    @Test
    @DisplayName("Should return false for invalid book ID")
    void testBorrowWithInvalidBookId() {
        boolean result = borrowService.borrowBook("INVALID", "M001");
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false for invalid member ID")
    void testBorrowWithInvalidMemberId() {
        boolean result = borrowService.borrowBook("B001", "INVALID");
        assertFalse(result);
    }
}
