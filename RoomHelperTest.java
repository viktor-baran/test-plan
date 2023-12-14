import com.securitySimulator.model.entities.Room;
import com.securitySimulator.model.enums.NormativeType;
import com.securitySimulator.model.sensor.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoomHelperTest {

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
    }

    @Test
    void testPopulateSensorsForRoomCommon() {
        room.setAmountOfDoors(2);
        room.setAmountOfWindows(3);
        room.setNormativeType(NormativeType.Common);

        RoomHelper.populateSensorsForRoom(room);

        List<Sensor> sensors = room.getSensorsForRoom();
        assertNotNull(sensors);
        assertEquals(3, sensors.size()); // Assuming totalSensors / 2 is 3 for Common
    }

    @Test
    void testPopulateSensorsForRoomPremium() {
        room.setAmountOfDoors(2);
        room.setAmountOfWindows(3);
        room.setNormativeType(NormativeType.Premium);

        RoomHelper.populateSensorsForRoom(room);

        List<Sensor> sensors = room.getSensorsForRoom();
        assertNotNull(sensors);
        assertEquals(5, sensors.size()); // Assuming totalSensors / 2 is 3 for Premium
    }

    @Test
    void testPopulateSensorsForRoomAdvanced() {
        room.setAmountOfDoors(2);
        room.setAmountOfWindows(3);
        room.setNormativeType(NormativeType.Advanced);

        RoomHelper.populateSensorsForRoom(room);

        List<Sensor> sensors = room.getSensorsForRoom();
        assertNotNull(sensors);
        assertEquals(6, sensors.size()); // Assuming totalSensors is 6 for Advanced
    }
}
