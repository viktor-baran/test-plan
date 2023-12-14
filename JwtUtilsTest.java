import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Date;

class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsImpl userDetails;

    private static final String USERNAME = "testUser";
    private static final String SECRET = "testSecret";
    private static final int EXPIRATION_MS = 3600000;

    @Test
    void generateJwtToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", EXPIRATION_MS);

        String token = jwtUtils.generateJwtToken(userDetails);

        assertNotNull(token);
    }

    @Test
    void generateTokenFromUsername() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", EXPIRATION_MS);

        String token = jwtUtils.generateTokenFromUsername(USERNAME);

        assertNotNull(token);
    }

    @Test
    void getUserNameFromJwtToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", SECRET);

        String token = jwtUtils.generateTokenFromUsername(USERNAME);
        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals(USERNAME, username);
    }

    @Test
    void validateJwtToken_ValidToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", SECRET);

        String token = jwtUtils.generateTokenFromUsername(USERNAME);
        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_InvalidToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", SECRET);

        // Creating an invalid token by modifying the signature
        String invalidToken = jwtUtils.generateTokenFromUsername(USERNAME) + "invalidSignature";
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        assertFalse(isValid);
    }
}
