package after.model;

/**
 * ✅ O - Adding a new member type = new class, zero changes to existing code.
 * ✅ L - Honors the Member contract fully.
 */
public class StandardMember extends Member {

    public StandardMember(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public int getBorrowLimit() { return 3; }

    @Override
    public String getMemberType() { return "STANDARD"; }
}
