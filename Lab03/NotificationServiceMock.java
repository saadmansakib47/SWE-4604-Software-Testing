import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class NotificationServiceMockTest {

    @Test
    void test_sendLoginNotification_shouldCallSendEmailExactlyOnce() {

        NotificationService mockService = mock(NotificationService.class);
        AuthService authService = new AuthService();
        String username = "mockUser";
        String expectedMessage = "User mockUser has logged in.";

        authService.sendLoginNotification(mockService, username);

        verify(mockService, times(1)).sendEmail(username, expectedMessage);
    }
}
