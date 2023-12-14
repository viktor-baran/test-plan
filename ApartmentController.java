import com.securitySimulator.controller.ApartmentController;
import com.securitySimulator.model.entities.Apartment;
import com.securitySimulator.repository.ApartmentRepository;
import com.securitySimulator.repository.UserRepository;
import com.securitySimulator.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ApartmentControllerTest {

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApartmentController apartmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllApartments() {
        when(apartmentRepository.findAll()).thenReturn(Arrays.asList(new Apartment(1L, 2), new Apartment(2L, 3)));

        ResponseEntity<List<Apartment>> response = apartmentController.getAllApartments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetApartmentsByUserId() {
        User user = new User();
        user.setId(1L);

        Apartment apartment1 = new Apartment(1L, 2);
        Apartment apartment2 = new Apartment(2L, 3);

        user.setBuildings(Arrays.asList(new Building(Arrays.asList(apartment1, apartment2))));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<List<Apartment>> response = apartmentController.GetApartmentsByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}
