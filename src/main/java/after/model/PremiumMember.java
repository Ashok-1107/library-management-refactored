package after.model;

/**
 * ✅ O - New member type added without touching any existing class.
 * ✅ L - Honors the Member contract. getBorrowLimit() returns a valid int.
 */
public class PremiumMember extends Member {

    public PremiumMember(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public int getBorrowLimit() { return 10; }

    @Override
    public String getMemberType() { return "PREMIUM"; }
}
