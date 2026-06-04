package after.notification;

/**
 * ✅ D - Dependency Inversion: high-level classes depend on this abstraction,
 *        not on a concrete EmailNotifier or SmsNotifier.
 * ✅ I - Interface Segregation: this interface has only one focused method.
 */
public interface NotificationService {
    void sendNotification(String recipient, String subject, String message);
}
