import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import static org.mockito.Mockito.*;

public class ClientMapSocketServiceTest {

    private ClientMapSocketService clientMapSocketService;

    @Before
    public void setUp() throws IOException {
        clientMapSocketService = new ClientMapSocketService();
    }

    @After
    public void tearDown() throws IOException {
        clientMapSocketService.StopConnection();
    }

    @Test
    public void testSendInfo() throws IOException {
        // Arrange
        Object obj = new HashMap<>(); // replace with your actual object

        // Mock the clientSocket and out
        Socket mockSocket = mock(Socket.class);
        BufferedWriter mockOut = mock(BufferedWriter.class);

        when(mockSocket.getOutputStream()).thenReturn(mockOut);
        when(mockSocket.isClosed()).thenReturn(false);

        ClientMapSocketService.clientSocket = mockSocket;

        // Act
        clientMapSocketService.SendInfo(obj);

        // Assert
        verify(mockOut, times(1)).write(anyString());
        verify(mockOut, times(1)).flush();

        // Make sure clientSocket and out are closed
        verify(mockSocket, times(1)).close();
        verify(mockOut, times(1)).close();
    }

    @Test
    public void testStopConnection() throws IOException {
        // Act
        clientMapSocketService.StopConnection();

        // Assert
        // Make sure the server socket is closed
        assertTrue(ClientMapSocketService.server.isClosed());
    }
}
