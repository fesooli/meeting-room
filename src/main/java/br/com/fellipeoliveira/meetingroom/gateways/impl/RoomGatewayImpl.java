package br.com.fellipeoliveira.meetingroom.gateways.impl;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.exceptions.NotFoundException;
import br.com.fellipeoliveira.meetingroom.gateways.RoomGateway;
import br.com.fellipeoliveira.meetingroom.gateways.repository.RoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoomGatewayImpl implements RoomGateway {

  private final RoomRepository roomRepository;

  @Override
  public void saveRoom(Room room) {
    roomRepository.save(room);
  }

  @Override
  public void deleteRoom(Integer roomId) {
    roomRepository.deleteById(roomId);
  }

  @Override
  public Room findRoomById(Integer roomId) {
    return roomRepository
        .findById(roomId)
        .orElseThrow(() -> new NotFoundException("Room not found!"));
  }

  @Override
  public Optional<Room> findRoomByRoomNumber(Integer roomNumber) {
    return roomRepository.findRoomByRoomNumber(roomNumber);
  }

  @Override
  public List<Room> listRooms() {
    return roomRepository.findAll();
  }
}
