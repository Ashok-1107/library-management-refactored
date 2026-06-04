package after.notification;

/**
 * ✅ O - New notification channel added with zero changes to existing code.
 * ✅ D - Pluggable via the NotificationService interface.
 */
public class SmsNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String subject, String message) {
        System.out.println("SMS → " + recipient + " | " + message);
    }
}
