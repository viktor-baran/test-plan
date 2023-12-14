import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.securitySimulator.exception.TokenRefreshException;
import com.securitySimulator.model.entities.Apartment;
import com.securitySimulator.model.user.*;
import com.securitySimulator.payload.request.LoginRequest;
import com.securitySimulator.payload.request.SignupRequest;
import com.securitySimulator.payload.request.TokenRefreshRequest;
import com.securitySimulator.payload.response.JwtResponse;
import com.securitySimulator.payload.response.MessageResponse;
import com.securitySimulator.payload.response.TokenRefreshResponse;
import com.securitySimulator.repository.RoleRepository;
import com.securitySimulator.repository.UserRepository;
import com.securitySimulator.security.jwt.JwtUtils;
import com.securitySimulator.security.services.RefreshTokenService;
import com.securitySimulator.security.services.UserDetailsServiceImpl;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Test
    void testAuthenticateUser() {
        // Mocking
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("mockedJwtToken");
        when(userDetails.getAuthorities()).thenReturn(new HashSet<>());
        when(refreshTokenService.createRefreshToken(anyLong())).thenReturn(new RefreshToken("mockedToken"));

        // Execution
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertNotNull(jwtResponse.getJwt());
        assertNotNull(jwtResponse.getRefreshToken());
    }

    @Test
    void testRegisterUser() {
        // Mocking
        SignupRequest signupRequest = new SignupRequest("newUser", "newUser@example.com", "password");
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(roleRepository.findByName(any())).thenReturn(java.util.Optional.of(new Role(ERole.ROLE_USER)));
        when(encoder.encode(any())).thenReturn("encodedPassword");

        // Execution
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());
    }

    @Test
    void testRefreshToken() {
        // Mocking
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest("refreshToken");
        RefreshToken refreshToken = new RefreshToken("refreshToken");
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "testEmail", "testPassword", new HashSet<>());
        when(refreshTokenService.findByToken(any())).thenReturn(java.util.Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
        when(refreshToken.getUser()).thenReturn(userDetails);
        when(jwtUtils.generateTokenFromUsername("testUser")).thenReturn("newJwtToken");

        // Execution
        ResponseEntity<?> responseEntity = authController.refreshtoken(tokenRefreshRequest);

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof TokenRefreshResponse);
        TokenRefreshResponse tokenRefreshResponse = (TokenRefreshResponse) responseEntity.getBody();
        assertEquals("newJwtToken", tokenRefreshResponse.getJwtToken());
        assertEquals("refreshToken", tokenRefreshResponse.getRefreshToken());
    }

    @Test
    void testDeleteRefreshToken() {
        // Mocking
        long userId = 1L;
        doNothing().when(refreshTokenService).deleteByUserId(userId);

        // Execution
        ResponseEntity<?> responseEntity = authController.deleterRefreshToken(userId);

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertEquals("refresh token deleted successfully sweaty balls", messageResponse.getMessage());
    }

    @Test
    void testGetAllUsers() {
        // Mocking
        User user = new User("testUser", "testEmail", "testPassword");
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Execution
        ResponseEntity<List<User>> responseEntity = authController.getAllUsers();

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("testUser", responseEntity.getBody().get(0).getUsername());
    }
}
