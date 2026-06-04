package after.model;

/**
 * ✅ S - Single Responsibility: Book only holds book data.
 */
public class Book {

    private final String id;
    private final String title;
    private final String author;
    private boolean available;

    public Book(String id, String title, String author) {
        this.id        = id;
        this.title     = title;
        this.author    = author;
        this.available = true;
    }

    public String getId()     { return id; }
    public String getTitle()  { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }

    public void markBorrowed()  { this.available = false; }
    public void markAvailable() { this.available = true; }

    @Override
    public String toString() {
        return id + " | " + title + " by " + author + " [" + (available ? "AVAILABLE" : "BORROWED") + "]";
    }
}
