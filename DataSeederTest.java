import com.securitySimulator.model.enums.NormativeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @InjectMocks
    private DataSeeder dataSeeder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private BuildingRepository buildingRepository;

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private FloorRepository floorRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private PasswordEncoder encoder;

    @Test
    void run() {
        // Mock necessary dependencies if needed

        // Call the method to be tested
        dataSeeder.run();

        // Verify that the saveAll method is called on the userRepository
        verify(userRepository, times(1)).saveAll(anyList());

        // Add more verification as needed based on your specific logic
    }

    @Test
    void getRandomBigDecimalInRange() {
        // Call the method to be tested
        BigDecimal result = DataSeeder.getRandomBigDecimalInRange(BigDecimal.ZERO, BigDecimal.TEN);

        // Add assertions based on the expected behavior of the method
        // For example, you can check if the result is within the specified range
        assertTrue(result.compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(result.compareTo(BigDecimal.TEN) <= 0);
    }

    // Add more test methods as needed for other functionalities in DataSeeder
}
