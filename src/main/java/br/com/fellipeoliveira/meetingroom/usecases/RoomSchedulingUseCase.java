package br.com.fellipeoliveira.meetingroom.usecases;

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
  private final ValidateUseCase validateUseCase;
  private final BuilderUtil builderUtil;

  public List<SchedulingResponseDTO> execute() {
    log.info("Getting schedules...");
    return builderUtil.buildRoomSchedulesResponse(roomSchedulingGateway.getSchedules());
  }

  public List<SchedulingResponseDTO> execute(Integer roomId, LocalDate initialDate, LocalDate finalDate) {
    return builderUtil.buildRoomSchedulesResponse(
        roomSchedulingGateway.getSchedulesByParameters(roomId, initialDate, finalDate));
  }

  public void execute(SchedulingDTO schedulingDTO) {
    validateUseCase.execute(schedulingDTO);
    roomSchedulingGateway.saveScheduling(
        builderUtil.buildRoomScheduling(schedulingDTO));
  }

  public void execute(Long schedulingId) {
    roomSchedulingGateway.deleteScheduling(schedulingId);
  }
}
