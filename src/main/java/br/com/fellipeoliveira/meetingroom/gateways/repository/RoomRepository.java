package br.com.fellipeoliveira.meetingroom.gateways.repository;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {

  Optional<Room> findRoomByRoomNumber(Integer roomNumber);

}
