package br.com.fellipeoliveira.meetingroom.gateways;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import java.util.List;
import java.util.Optional;

public interface RoomGateway {

  void saveRoom(Room room);

  void deleteRoom(Integer roomId);

  Room findRoomById(Integer roomId);

  Optional<Room> findRoomByRoomNumber(Integer roomNumber);

  List<Room> listRooms();

}
