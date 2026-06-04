package after.model;

/**
 * ✅ O - Another member type added without changing existing code.
 * ✅ L - Honors the Member contract fully.
 */
public class StudentMember extends Member {

    public StudentMember(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public int getBorrowLimit() { return 2; }

    @Override
    public String getMemberType() { return "STUDENT"; }
}
