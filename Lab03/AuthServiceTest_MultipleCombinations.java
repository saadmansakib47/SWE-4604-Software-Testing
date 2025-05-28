import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceParameterizedTest {

    private final AuthService authService = new AuthService();

    @ParameterizedTest(name = "isValidUser(\"{0}\", \"{1}\") should return {2}")
    @DisplayName("Test multiple combinations of usernames and passwords")
    @CsvSource({
        "john, secret123, true",
        "alice, pass, false",        
        "'', abcdef, false",         
        "bob, , false",                     
        "krita, abcdefghijklmnopqr, true",  
        "sunny, abcdefghij12345678, true",  
        "sunny, abcdef, false"      
    })
    void test_isValidUser_withMultipleInputs(String username, String password, boolean expected) {
        boolean result = authService.isValidUser(username, password);
        assertEquals(expected, result, 
            () -> String.format("Expected isValidUser(%s, %s) to be %s", username, password, expected));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Test null password returns false")
    void test_isValidUser_withNullPassword(String password) {
        boolean result = authService.isValidUser("anyuser", password);
        assertFalse(result);
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("Test empty username returns false")
    void test_isValidUser_withEmptyUsername(String username) {
        boolean result = authService.isValidUser(username, "pass123");
        assertFalse(result);
    }

}
