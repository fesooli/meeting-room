package br.com.fellipeoliveira.meetingroom.gateways.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import br.com.fellipeoliveira.meetingroom.exceptions.NotFoundException;
import br.com.fellipeoliveira.meetingroom.gateways.repository.RoomRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoomGatewayImplTest {

  @InjectMocks
  private RoomGatewayImpl roomGateway;

  @Mock
  private RoomRepository roomRepository;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void saveRoom() {
    final Room room = getRoom();

    roomGateway.saveRoom(room);

    verify(roomRepository, times(1)).save(room);
  }

  @Test
  public void deleteRoom() {
    roomGateway.deleteRoom(1);

    verify(roomRepository, times(1)).deleteById(1);
  }

  @Test
  public void findRoomById() {
    when(roomRepository.findById(1)).thenReturn(Optional.of(Room.builder().roomId(1).build()));

    roomGateway.findRoomById(1);

    verify(roomRepository, times(1)).findById(1);
  }

  @Test(expected = NotFoundException.class)
  public void findRoomByIdWithError() {
    roomGateway.findRoomById(1);

    verify(roomRepository, times(1)).findById(1);
  }

  @Test
  public void findRoomByRoomNumber() {
    roomGateway.findRoomByRoomNumber(1);

    verify(roomRepository, times(1)).findRoomByRoomNumber(1);
  }

  @Test
  public void listRooms() {
    roomGateway.listRooms();

    verify(roomRepository, times(1)).findAll();
  }

  private Room getRoom() {
    return Room.builder().roomName("Sala 01").roomNumber(1).roomId(1).build();
  }

  private Room getRoom2() {
    return Room.builder().roomName("Sala 01").roomNumber(1).roomId(1).build();
  }
}