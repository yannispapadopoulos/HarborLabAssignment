package gr.harborlab.service;

import gr.harborlab.entities.Room;
import gr.harborlab.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRooms_Success() {
        Room room1 = new Room();
        room1.setId(1L);
        room1.setName("Conference Room A");

        Room room2 = new Room();
        room2.setId(2L);
        room2.setName("Conference Room B");

        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));

        List<Room> rooms = roomService.getAllRooms();

        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertEquals("Conference Room A", rooms.get(0).getName());
        verify(roomRepository, times(1)).findAll();
    }
}