package after.service;

import after.model.Book;
import after.model.BorrowRecord;
import after.model.Member;
import after.notification.NotificationService;
import after.repository.BookRepository;
import after.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ✅ S - Only responsible for borrow and return operations.
 * ✅ D - Depends on abstractions: BookRepository, MemberRepository, NotificationService.
 * ✅ O - New member types automatically get their borrow limit from Member.getBorrowLimit().
 */
public class BorrowService {

    private final BookRepository       bookRepository;
    private final MemberRepository     memberRepository;
    private final NotificationService  notificationService;
    private final List<BorrowRecord>   borrowRecords = new ArrayList<>();

    public BorrowService(BookRepository bookRepository,
                         MemberRepository memberRepository,
                         NotificationService notificationService) {
        this.bookRepository      = bookRepository;
        this.memberRepository    = memberRepository;
        this.notificationService = notificationService;
    }

    public boolean borrowBook(String bookId, String memberId) {
        Optional<Book>   bookOpt   = bookRepository.findById(bookId);
        Optional<Member> memberOpt = memberRepository.findById(memberId);

        if (bookOpt.isEmpty() || memberOpt.isEmpty()) return false;

        Book   book   = bookOpt.get();
        Member member = memberOpt.get();

        if (!book.isAvailable()) {
            System.out.println("Book not available: " + book.getTitle());
            return false;
        }

        // ✅ O - No if-else for member type. Borrow limit comes from the member itself.
        long borrowed = borrowRecords.stream()
                .filter(r -> r.getMemberId().equals(memberId))
                .count();

        if (borrowed >= member.getBorrowLimit()) {
            System.out.println("Borrow limit reached for: " + member.getName());
            return false;
        }

        book.markBorrowed();
        borrowRecords.add(new BorrowRecord(bookId, memberId,
                LocalDate.now(), LocalDate.now().plusDays(14)));

        notificationService.sendNotification(
            member.getEmail(),
            "Book Borrowed",
            "You borrowed \"" + book.getTitle() + "\". Due: " + LocalDate.now().plusDays(14)
        );
        return true;
    }

    public boolean returnBook(String bookId, String memberId) {
        Optional<Book>   bookOpt   = bookRepository.findById(bookId);
        Optional<Member> memberOpt = memberRepository.findById(memberId);

        if (bookOpt.isEmpty() || memberOpt.isEmpty()) return false;

        Book   book   = bookOpt.get();
        Member member = memberOpt.get();

        book.markAvailable();
        borrowRecords.removeIf(r -> r.getBookId().equals(bookId)
                                 && r.getMemberId().equals(memberId));

        notificationService.sendNotification(
            member.getEmail(),
            "Book Returned",
            "Thank you for returning \"" + book.getTitle() + "\"."
        );
        return true;
    }

    public List<BorrowRecord> getBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }

    public long countBorrowedByMember(String memberId) {
        return borrowRecords.stream()
                .filter(r -> r.getMemberId().equals(memberId))
                .count();
    }
}
