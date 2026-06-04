package after;

import after.model.*;
import after.notification.NotificationService;
import after.repository.InMemoryMemberRepository;
import after.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MemberService Tests")
class MemberServiceTest {

    private MemberService memberService;

    // Simple test double — no Mockito needed, keeps it clean
    private static class FakeNotifier implements NotificationService {
        String lastRecipient;
        String lastSubject;

        @Override
        public void sendNotification(String recipient, String subject, String message) {
            this.lastRecipient = recipient;
            this.lastSubject   = subject;
        }
    }

    private FakeNotifier fakeNotifier;

    @BeforeEach
    void setUp() {
        fakeNotifier  = new FakeNotifier();
        memberService = new MemberService(new InMemoryMemberRepository(), fakeNotifier);
    }

    @Test
    @DisplayName("Should register a standard member")
    void testRegisterStandardMember() {
        Member member = new StandardMember("M001", "Ashok", "ashok@example.com");
        memberService.registerMember(member);

        Optional<Member> result = memberService.findMember("M001");
        assertTrue(result.isPresent());
        assertEquals("Ashok", result.get().getName());
    }

    @Test
    @DisplayName("Standard member should have borrow limit of 3")
    void testStandardMemberBorrowLimit() {
        Member member = new StandardMember("M001", "Ashok", "ashok@example.com");
        assertEquals(3, member.getBorrowLimit());
    }

    @Test
    @DisplayName("Premium member should have borrow limit of 10")
    void testPremiumMemberBorrowLimit() {
        Member member = new PremiumMember("M002", "Raj", "raj@example.com");
        assertEquals(10, member.getBorrowLimit());
    }

    @Test
    @DisplayName("Student member should have borrow limit of 2")
    void testStudentMemberBorrowLimit() {
        Member member = new StudentMember("M003", "Priya", "priya@example.com");
        assertEquals(2, member.getBorrowLimit());
    }

    @Test
    @DisplayName("Should send welcome notification on registration")
    void testWelcomeNotificationSent() {
        Member member = new StandardMember("M001", "Ashok", "ashok@example.com");
        memberService.registerMember(member);

        assertEquals("ashok@example.com", fakeNotifier.lastRecipient);
        assertEquals("Welcome to the Library!", fakeNotifier.lastSubject);
    }

    @Test
    @DisplayName("Should return empty for non-existent member")
    void testFindNonExistentMember() {
        Optional<Member> result = memberService.findMember("INVALID");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return all registered members")
    void testGetAllMembers() {
        memberService.registerMember(new StandardMember("M001", "Ashok", "a@example.com"));
        memberService.registerMember(new PremiumMember("M002", "Raj", "r@example.com"));

        assertEquals(2, memberService.getAllMembers().size());
    }
}
