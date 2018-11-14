package br.com.fellipeoliveira.meetingroom.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import br.com.fellipeoliveira.meetingroom.gateways.RoomSchedulingGateway;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.SchedulingDTO;
import br.com.fellipeoliveira.meetingroom.util.BuilderUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoomSchedulingUseCaseTest {

  @InjectMocks private RoomSchedulingUseCase roomSchedulingUseCase;

  @Mock private RoomSchedulingGateway roomSchedulingGateway;

  @Mock private ValidateUseCase validateUseCase;

  @Mock private BuilderUtil builderUtil;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void findSchedules() {
    roomSchedulingUseCase.execute();

    verify(roomSchedulingGateway, times(1)).getSchedules();
    verify(builderUtil, times(1)).buildRoomSchedulesResponse(any());
  }

  @Test
  public void findSchedulesWithParameters() {
    roomSchedulingUseCase.execute(any(), any(), any());

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedulesResponse(any());
  }

  @Test
  public void saveSchedule() {
    final SchedulingDTO schedulingDTO = getSchedulingDTO();
    when(builderUtil.buildRoomScheduling(schedulingDTO))
        .thenReturn(getRoomScheduling(schedulingDTO));

    roomSchedulingUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).saveScheduling(getRoomScheduling(schedulingDTO));
    verify(builderUtil, times(1)).buildRoomScheduling(schedulingDTO);
    verify(validateUseCase, times(1)).execute(schedulingDTO);
  }

  @Test
  public void deleteSchedule() {
    roomSchedulingUseCase.execute(1L);

    verify(roomSchedulingGateway, times(1)).deleteScheduling(any());
  }

  private SchedulingDTO getSchedulingDTO() {
    return SchedulingDTO.builder()
        .scheduledTime(LocalTime.now().plusMinutes(60))
        .scheduledDate(LocalDate.now())
        .reservedTimeInMinutes(60)
        .room(RoomDTO.builder().roomId(1).roomNumber(1).roomName("Sala 01").build())
        .schedulingName("Teste")
        .build();
  }

  public RoomScheduling getRoomScheduling(SchedulingDTO schedulingDTO) {
    return RoomScheduling.builder()
        .scheduledTime(schedulingDTO.getScheduledTime())
        .scheduledDate(schedulingDTO.getScheduledDate())
        .reservedTimeInMinutes(schedulingDTO.getReservedTimeInMinutes())
        .room(getRoom(schedulingDTO.getRoom()))
        .schedulingName(schedulingDTO.getSchedulingName())
        .schedulingId(schedulingDTO.getId())
        .build();
  }

  public Room getRoom(RoomDTO roomDTO) {
    return Room.builder()
        .roomName(roomDTO.getRoomName())
        .roomNumber(roomDTO.getRoomNumber())
        .roomId(roomDTO.getRoomId())
        .build();
  }
}
