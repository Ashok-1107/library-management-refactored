package before;

import java.util.ArrayList;
import java.util.List;

/**
 * ❌ BAD CODE - LibraryManager (God Class)
 *
 * SOLID VIOLATIONS:
 * 1. S - Single Responsibility: This one class handles books, members, borrowing,
 *    notifications, and reporting. It should be split into separate classes.
 *
 * 2. O - Open/Closed: Adding a new notification type (e.g. SMS) means editing
 *    this class directly instead of extending it.
 *
 * 3. L - Liskov Substitution: PremiumMember extends Member but overrides
 *    borrowBook() in a way that breaks the expected behavior (throws exception
 *    when limit not exceeded, instead of allowing borrow).
 *
 * 4. I - Interface Segregation: The LibraryActions interface forces ALL
 *    implementors to implement methods they don't need.
 *
 * 5. D - Dependency Inversion: LibraryManager directly instantiates
 *    EmailNotifier — tightly coupled, impossible to swap or mock in tests.
 */
public class LibraryManager {

    // Hard-coded values scattered everywhere
    private static final int MAX_BORROW_LIMIT = 3;
    private static final double FINE_PER_DAY = 1.50;

    private List<String[]> books = new ArrayList<>();   // [id, title, author, status]
    private List<String[]> members = new ArrayList<>(); // [id, name, email, type]
    private List<String[]> borrowRecords = new ArrayList<>(); // [bookId, memberId, dueDate]

    // ❌ D violation: directly instantiating a concrete class
    private EmailNotifier emailNotifier = new EmailNotifier();

    // ─── Book Operations ───────────────────────────────────────────────

    public void addBook(String id, String title, String author) {
        books.add(new String[]{id, title, author, "AVAILABLE"});
        System.out.println("Book added: " + title);
    }

    public void removeBook(String bookId) {
        books.removeIf(b -> b[0].equals(bookId));
    }

    public String[] findBook(String bookId) {
        for (String[] book : books) {
            if (book[0].equals(bookId)) return book;
        }
        return null;
    }

    // ─── Member Operations ─────────────────────────────────────────────

    public void registerMember(String id, String name, String email, String type) {
        members.add(new String[]{id, name, email, type});
        // ❌ O violation: adding new member type means editing this if-else chain
        if (type.equals("PREMIUM")) {
            System.out.println("Premium member registered: " + name);
            emailNotifier.sendEmail(email, "Welcome Premium Member!", "Enjoy unlimited borrows.");
        } else if (type.equals("STANDARD")) {
            System.out.println("Standard member registered: " + name);
            emailNotifier.sendEmail(email, "Welcome!", "You can borrow up to 3 books.");
        } else if (type.equals("STUDENT")) {
            System.out.println("Student member registered: " + name);
            emailNotifier.sendEmail(email, "Welcome Student!", "Student discount applied.");
        }
    }

    // ─── Borrow / Return ───────────────────────────────────────────────

    public boolean borrowBook(String bookId, String memberId) {
        String[] book = findBook(bookId);
        String[] member = findMemberById(memberId);

        if (book == null || member == null) return false;
        if (!book[3].equals("AVAILABLE")) {
            System.out.println("Book not available");
            return false;
        }

        // ❌ S violation: borrow logic, fine calculation, AND notification all in one method
        int borrowedCount = countBorrowedBooks(memberId);

        // ❌ O violation: checking member type with if-else — every new type needs code change here
        int limit = MAX_BORROW_LIMIT;
        if (member[3].equals("PREMIUM")) {
            limit = 10;
        } else if (member[3].equals("STUDENT")) {
            limit = 2;
        }

        if (borrowedCount >= limit) {
            System.out.println("Borrow limit reached");
            return false;
        }

        book[3] = "BORROWED";
        borrowRecords.add(new String[]{bookId, memberId, "2026-07-04"});

        // ❌ D violation: hard-coded email notification, can't swap to SMS or push
        emailNotifier.sendEmail(member[2], "Book Borrowed", "You borrowed: " + book[1]);
        return true;
    }

    public boolean returnBook(String bookId, String memberId) {
        String[] book = findBook(bookId);
        if (book == null) return false;

        book[3] = "AVAILABLE";
        borrowRecords.removeIf(r -> r[0].equals(bookId) && r[1].equals(memberId));

        String[] member = findMemberById(memberId);
        if (member != null) {
            // ❌ S violation: return logic mixed with fine calculation mixed with notification
            double fine = calculateFine(bookId);
            if (fine > 0) {
                System.out.println("Fine applied: $" + fine);
                emailNotifier.sendEmail(member[2], "Fine Notice", "You have a fine of $" + fine);
            }
            emailNotifier.sendEmail(member[2], "Book Returned", "Thanks for returning: " + book[1]);
        }
        return true;
    }

    // ─── Fine Calculation ──────────────────────────────────────────────

    // ❌ S violation: fine calculation buried inside the manager class
    public double calculateFine(String bookId) {
        // Simplified: always 0 for now but logic lives here instead of own class
        return 0.0;
    }

    // ─── Report Generation ─────────────────────────────────────────────

    // ❌ S violation: reporting is NOT the library manager's responsibility
    public void printAvailableBooks() {
        System.out.println("=== Available Books ===");
        for (String[] book : books) {
            if (book[3].equals("AVAILABLE")) {
                System.out.println(book[0] + " | " + book[1] + " by " + book[2]);
            }
        }
    }

    public void printAllMembers() {
        System.out.println("=== All Members ===");
        for (String[] member : members) {
            System.out.println(member[0] + " | " + member[1] + " | " + member[3]);
        }
    }

    // ─── Helpers ───────────────────────────────────────────────────────

    private String[] findMemberById(String memberId) {
        for (String[] member : members) {
            if (member[0].equals(memberId)) return member;
        }
        return null;
    }

    private int countBorrowedBooks(String memberId) {
        int count = 0;
        for (String[] record : borrowRecords) {
            if (record[1].equals(memberId)) count++;
        }
        return count;
    }

    // ─── Inner class — also a violation: should be its own file ────────

    // ❌ D violation: concrete class, not behind an interface
    public static class EmailNotifier {
        public void sendEmail(String to, String subject, String body) {
            System.out.println("EMAIL to " + to + " | " + subject + " | " + body);
        }
    }
}
