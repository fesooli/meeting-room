package br.com.fellipeoliveira.meetingroom.gateways;

import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import java.time.LocalDate;
import java.util.List;

public interface RoomSchedulingGateway {

  void saveScheduling(RoomScheduling roomScheduling);

  void deleteScheduling(Long schedulingId);

  List<RoomScheduling> getSchedules();

  List<RoomScheduling> getSchedulesByParameters(Integer roomId, LocalDate initialDate, LocalDate finalDate);
}
