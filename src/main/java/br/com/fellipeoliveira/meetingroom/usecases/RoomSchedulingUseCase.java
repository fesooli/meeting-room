package br.com.fellipeoliveira.meetingroom.usecases;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import br.com.fellipeoliveira.meetingroom.exceptions.DateValidationException;
import br.com.fellipeoliveira.meetingroom.exceptions.NotFoundException;
import br.com.fellipeoliveira.meetingroom.gateways.RoomGateway;
import br.com.fellipeoliveira.meetingroom.gateways.RoomSchedulingGateway;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.SchedulingDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.response.SchedulingResponseDTO;
import br.com.fellipeoliveira.meetingroom.util.BuilderUtil;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RoomSchedulingUseCase {

  private final RoomSchedulingGateway roomSchedulingGateway;
  private final RoomGateway roomGateway;
  private final ValidateUseCase validateUseCase;
  private final BuilderUtil builderUtil;

  public List<SchedulingResponseDTO> execute() {
    log.info("Getting schedules...");
    return builderUtil.buildRoomSchedulesResponse(roomSchedulingGateway.getSchedules());
  }

  public List<SchedulingResponseDTO> execute(
      Integer roomId, LocalDate initialDate, LocalDate finalDate) {
    if (initialDate.isAfter(finalDate)) {
      throw new DateValidationException("The initial date can not be after the final date!");
    }
    return builderUtil.buildRoomSchedulesResponse(
        roomSchedulingGateway.getSchedulesByParameters(roomId, initialDate, finalDate));
  }

  public void execute(SchedulingDTO schedulingDTO) {
    if(schedulingDTO.getId() != null) {
      roomSchedulingGateway.findRoomSchedulingById(schedulingDTO.getId());
    }

    if (schedulingDTO.getRoom() != null && schedulingDTO.getRoom().getRoomId() != null) {
      validateUseCase.execute(schedulingDTO);
      final Room room = roomGateway.findRoomById(schedulingDTO.getRoom().getRoomId());
      final RoomScheduling roomScheduling = builderUtil.buildRoomScheduling(schedulingDTO);
      roomScheduling.setRoom(room);
      roomSchedulingGateway.saveScheduling(roomScheduling);
    } else {
      throw new NotFoundException("Room object can not be null");
    }
  }

  public void execute(Long schedulingId) {
    roomSchedulingGateway.deleteScheduling(schedulingId);
  }
}
