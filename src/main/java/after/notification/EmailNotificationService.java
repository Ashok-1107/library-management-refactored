package after.notification;

/**
 * ✅ D - Concrete implementation behind the NotificationService abstraction.
 *        Swap this for SmsNotificationService anytime without touching service code.
 */
public class EmailNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String subject, String message) {
        System.out.println("EMAIL → " + recipient + " | " + subject + " | " + message);
    }
}
