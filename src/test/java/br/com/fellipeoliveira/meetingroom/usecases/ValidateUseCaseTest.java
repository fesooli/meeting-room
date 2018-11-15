package br.com.fellipeoliveira.meetingroom.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fellipeoliveira.meetingroom.config.ScheduleConfig;
import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import br.com.fellipeoliveira.meetingroom.exceptions.SchedulingValidationException;
import br.com.fellipeoliveira.meetingroom.gateways.RoomSchedulingGateway;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.SchedulingDTO;
import br.com.fellipeoliveira.meetingroom.util.BuilderUtil;
import br.com.fellipeoliveira.meetingroom.util.ValidationUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ValidateUseCaseTest {

  @InjectMocks private ValidateUseCase validateUseCase;

  @Mock private RoomSchedulingGateway roomSchedulingGateway;

  @Mock private ValidationUtil validationUtil;

  @Mock private BuilderUtil builderUtil;

  @Mock private ScheduleConfig scheduleConfig;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void executeWithScheduling() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    final SchedulingDTO schedulingDTO = getSchedulingDTO();

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingRangeValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    final SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setScheduledDate(LocalDate.now().plusDays(1));
    schedulingDTO.setScheduledTime(LocalTime.of(20, 00));

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingRangeGreaterValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    final SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setScheduledDate(LocalDate.now().plusDays(1));
    schedulingDTO.setScheduledTime(LocalTime.of(06, 00));

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingMaximumTimeValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setReservedTimeInMinutes(1200);

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingMinimumTimeValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setReservedTimeInMinutes(10);

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingEarlierValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setScheduledDate(LocalDate.now().minusDays(1));

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test
  public void executeWithSchedulingAlreadyExistsValidationWithSuccess() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    when(builderUtil.buildRoomSchedules(any())).thenReturn(Arrays.asList(getSchedulingDTO()));

    SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setScheduledDate(LocalDate.now().plusDays(1));
    schedulingDTO.setScheduledTime(LocalTime.of(13, 00));

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingAlreadyExistsValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    when(builderUtil.buildRoomSchedules(any())).thenReturn(Arrays.asList(getSchedulingDTO()));

    SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setReservedTimeInMinutes(90);

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingAlreadyExistsSameTimeValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    when(builderUtil.buildRoomSchedules(any())).thenReturn(Arrays.asList(getSchedulingDTO()));

    SchedulingDTO schedulingDTO = getSchedulingDTO();

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingOnSaturdayValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    when(builderUtil.buildRoomSchedules(any())).thenReturn(Arrays.asList(getSchedulingDTO()));

    SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setScheduledDate(LocalDate.of(2018, 12, 22));
    schedulingDTO.setReservedTimeInMinutes(90);

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test(expected = SchedulingValidationException.class)
  public void executeWithSchedulingOnSundayValidationError() {
    when(scheduleConfig.getScheduleEndTime()).thenReturn("19:00:00");
    when(scheduleConfig.getScheduleStartTime()).thenReturn("07:00:00");
    when(scheduleConfig.getMaximumReserveTimeInMinutes()).thenReturn(720);
    when(scheduleConfig.getMinimumReserveTimeInMinutes()).thenReturn(15);
    when(builderUtil.buildRoomSchedules(any())).thenReturn(Arrays.asList(getSchedulingDTO()));

    SchedulingDTO schedulingDTO = getSchedulingDTO();
    schedulingDTO.setScheduledDate(LocalDate.of(2018, 12, 23));
    schedulingDTO.setReservedTimeInMinutes(90);

    validateUseCase.execute(schedulingDTO);

    verify(roomSchedulingGateway, times(1)).getSchedulesByParameters(any(), any(), any());
    verify(builderUtil, times(1)).buildRoomSchedules(any());
    verify(validationUtil, times(1)).validate(schedulingDTO);
  }

  @Test
  public void executeWithRoom() {
    final RoomDTO roomDTO = getRoomDTO();

    validateUseCase.execute(roomDTO);

    verify(validationUtil, times(1)).validate(roomDTO);
  }

  private SchedulingDTO getSchedulingDTO() {
    return SchedulingDTO.builder()
        .scheduledTime(LocalTime.of(8, 00))
        .scheduledDate(LocalDate.of(2018, 12, 10))
        .reservedTimeInMinutes(60)
        .room(new RoomDTO())
        .schedulingName("Teste")
        .build();
  }

  private RoomDTO getRoomDTO() {
    return RoomDTO.builder().roomName("Sala 01").roomNumber(1).roomId(1).build();
  }
}
