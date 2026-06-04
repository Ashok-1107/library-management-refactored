package after.repository;

import after.model.Member;
import java.util.*;

/**
 * ✅ S - Only responsible for storing and retrieving members.
 */
public class InMemoryMemberRepository implements MemberRepository {

    private final Map<String, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
