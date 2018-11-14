package br.com.fellipeoliveira.meetingroom.util;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.SchedulingDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ValidationUtilTest {

  @InjectMocks
  private ValidationUtil validationUtil;

  @Mock
  private Validator validator;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void validateWithScheduling() {
    final SchedulingDTO schedulingDTO = getSchedulingDTO();

    validationUtil.validate(schedulingDTO);

    verify(validator, times(1)).validate(schedulingDTO);
  }

  @Test
  public void validateWithRoom() {
    final RoomDTO roomDTO = getRoomDTO();

    validationUtil.validate(roomDTO);

    verify(validator, times(1)).validate(roomDTO);
  }

  private SchedulingDTO getSchedulingDTO() {
    return SchedulingDTO.builder()
        .scheduledTime(LocalTime.now().plusMinutes(60))
        .scheduledDate(LocalDate.now())
        .reservedTimeInMinutes(60)
        .room(new RoomDTO())
        .schedulingName("Teste")
        .build();
  }

  private RoomDTO getRoomDTO() {
    return RoomDTO.builder()
        //.roomName("Sala 01")
        .roomNumber(1)
        .roomId(1)
        .build();
  }

}