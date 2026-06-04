package after.model;

/**
 * ✅ S - Single Responsibility: Member only holds member data + borrow limit rule.
 * ✅ O - Open/Closed: New member types extend this class, no existing code changes.
 * ✅ L - Liskov Substitution: All subclasses honor the borrowBook contract.
 */
public abstract class Member {

    private final String id;
    private final String name;
    private final String email;

    public Member(String id, String name, String email) {
        this.id    = id;
        this.name  = name;
        this.email = email;
    }

    public String getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }

    /**
     * Each member type defines its own borrow limit.
     * ✅ O - extend by adding a new subclass, never by editing this method.
     */
    public abstract int getBorrowLimit();

    /**
     * Friendly label for welcome messages.
     */
    public abstract String getMemberType();
}
