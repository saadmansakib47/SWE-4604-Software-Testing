import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(OrderAnnotation.class)
public class AuthServiceTest {

    private static AuthService authService;
    private static final String validUsername = "saadman";
    private static final String validPassword = "here321";

    @BeforeAll
    static void setup() {
        authService = new AuthService();
        System.out.println("Starting AuthService Tests.");
    }

    @Test
    @Order(1)
    void test_1_isValidUser_shouldReturnTrueForValidInput() {
        boolean result = authService.isValidUser(validUsername, validPassword);
        System.out.println("Executed test_1_isValidUser");
        assertTrue(result);
    }

    @Test
    @Order(2)
    void test_2_generateToken_shouldReturnFormattedToken() {
        String token = authService.generateToken(validUsername);
        System.out.println("Executed test_2_generateToken");
        assertEquals("TOKEN_SAADMAN", token);
    }

    @Test
    @Order(3)
    void test_3_sendLoginNotification_shouldCallNotificationService() {
        NotificationService mockService = Mockito.mock(NotificationService.class);
        authService.sendLoginNotification(mockService, validUsername);

        System.out.println("Executed test_3_sendLoginNotification");

        verify(mockService, times(1))
            .sendEmail(eq(validUsername), eq("User saadman has logged in."));
    }

    @AfterAll
    static void teardown() {
        System.out.println("Completed AuthService Tests.");
    }
}
