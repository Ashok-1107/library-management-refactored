package after.model;

import java.time.LocalDate;

/**
 * ✅ S - Simple value object holding borrow record data only.
 */
public class BorrowRecord {

    private final String bookId;
    private final String memberId;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;

    public BorrowRecord(String bookId, String memberId, LocalDate borrowDate, LocalDate dueDate) {
        this.bookId     = bookId;
        this.memberId   = memberId;
        this.borrowDate = borrowDate;
        this.dueDate    = dueDate;
    }

    public String getBookId()       { return bookId; }
    public String getMemberId()     { return memberId; }
    public LocalDate getBorrowDate(){ return borrowDate; }
    public LocalDate getDueDate()   { return dueDate; }
}
