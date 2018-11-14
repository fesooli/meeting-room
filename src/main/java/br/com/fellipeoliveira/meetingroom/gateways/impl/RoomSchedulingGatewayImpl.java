package br.com.fellipeoliveira.meetingroom.gateways.impl;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import br.com.fellipeoliveira.meetingroom.gateways.RoomGateway;
import br.com.fellipeoliveira.meetingroom.gateways.RoomSchedulingGateway;
import br.com.fellipeoliveira.meetingroom.gateways.repository.RoomSchedulingRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoomSchedulingGatewayImpl implements RoomSchedulingGateway {

  private final RoomSchedulingRepository roomSchedulingRepository;
  private final RoomGateway roomGateway;

  @Override
  public void saveScheduling(RoomScheduling roomScheduling) {
    roomSchedulingRepository.save(roomScheduling);
  }

  @Override
  public void deleteScheduling(Long schedulingId) {
    roomSchedulingRepository.deleteById(schedulingId);
  }

  @Override
  public List<RoomScheduling> getSchedules() {
    return roomSchedulingRepository.findAll();
  }

  @Override
  public List<RoomScheduling> getSchedulesByParameters(
      Integer roomId, LocalDate initialDate, LocalDate finalDate) {
    if (roomId.compareTo(0) == 0) {
      return roomSchedulingRepository.findAllByScheduledDateBetween(initialDate, finalDate);
    } else {
      final Room room = roomGateway.findRoomById(roomId);
      return roomSchedulingRepository.findAllByScheduledDateAndRoom(initialDate, room);
    }
  }
}
