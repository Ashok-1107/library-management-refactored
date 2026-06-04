package after.service;

import after.model.Member;
import after.notification.NotificationService;
import after.repository.MemberRepository;
import java.util.List;
import java.util.Optional;

/**
 * ✅ S - Only responsible for member registration and lookup.
 * ✅ D - Depends on MemberRepository and NotificationService abstractions.
 * ✅ O - New member types work automatically via Member.getMemberType().
 */
public class MemberService {

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public MemberService(MemberRepository memberRepository,
                         NotificationService notificationService) {
        this.memberRepository    = memberRepository;
        this.notificationService = notificationService;
    }

    public void registerMember(Member member) {
        memberRepository.save(member);
        // ✅ O - No if-else for member type. Each member knows its own type label.
        notificationService.sendNotification(
            member.getEmail(),
            "Welcome to the Library!",
            "Hi " + member.getName() + ", you are registered as a "
                + member.getMemberType() + " member. Borrow limit: " + member.getBorrowLimit()
        );
        System.out.println(member.getMemberType() + " member registered: " + member.getName());
    }

    public Optional<Member> findMember(String memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
