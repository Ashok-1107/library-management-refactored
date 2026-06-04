package after.repository;

import after.model.Member;
import java.util.List;
import java.util.Optional;

/**
 * ✅ D - Service layer depends on this abstraction.
 * ✅ I - Focused only on member persistence operations.
 */
public interface MemberRepository {
    void save(Member member);
    Optional<Member> findById(String id);
    List<Member> findAll();
}
