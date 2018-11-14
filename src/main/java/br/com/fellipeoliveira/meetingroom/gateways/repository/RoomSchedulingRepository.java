package br.com.fellipeoliveira.meetingroom.gateways.repository;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomSchedulingRepository extends JpaRepository<RoomScheduling, Long> {

  List<RoomScheduling> findAllByScheduledDateBetween(LocalDate initialDate, LocalDate finalDate);

  List<RoomScheduling> findAllByScheduledDateAndRoom(LocalDate scheduledDate, Room room);

  List<RoomScheduling> findByRoom(Room room);

}
