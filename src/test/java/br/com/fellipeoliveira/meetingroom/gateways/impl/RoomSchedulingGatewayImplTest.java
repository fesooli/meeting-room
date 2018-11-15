package br.com.fellipeoliveira.meetingroom.gateways.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import br.com.fellipeoliveira.meetingroom.exceptions.NotFoundException;
import br.com.fellipeoliveira.meetingroom.gateways.RoomGateway;
import br.com.fellipeoliveira.meetingroom.gateways.repository.RoomSchedulingRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoomSchedulingGatewayImplTest {

  @InjectMocks private RoomSchedulingGatewayImpl roomSchedulingGateway;

  @Mock private RoomSchedulingRepository roomSchedulingRepository;

  @Mock private RoomGateway roomGateway;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void saveScheduling() {
    roomSchedulingGateway.saveScheduling(new RoomScheduling());

    verify(roomSchedulingRepository, times(1)).save(new RoomScheduling());
  }

  @Test
  public void deleteScheduling() {
    roomSchedulingGateway.deleteScheduling(1L);

    verify(roomSchedulingRepository, times(1)).deleteById(1L);
  }

  @Test
  public void getSchedules() {
    roomSchedulingGateway.getSchedules();

    verify(roomSchedulingRepository, times(1)).findAll();
  }

  @Test
  public void findRoomSchedulingById() {
    when(roomSchedulingRepository.findById(1L))
        .thenReturn(Optional.of(RoomScheduling.builder().build()));

    roomSchedulingGateway.findRoomSchedulingById(1L);

    verify(roomSchedulingRepository, times(1)).findById(1L);
  }

  @Test(expected = NotFoundException.class)
  public void findRoomSchedulingByIdWithError() {
    roomSchedulingGateway.findRoomSchedulingById(1L);
  }

  @Test
  public void getSchedulesByParametersWithZeroRoom() {
    roomSchedulingGateway.getSchedulesByParameters(0, LocalDate.now(), LocalDate.now());

    verify(roomSchedulingRepository, times(1))
        .findAllByScheduledDateBetween(LocalDate.now(), LocalDate.now());
  }

  @Test
  public void getSchedulesByParameters() {
    when(roomGateway.findRoomById(1)).thenReturn(Room.builder().roomId(1).build());
    when(roomSchedulingRepository.findAllByScheduledDate(LocalDate.now()))
        .thenReturn(
            Arrays.asList(RoomScheduling.builder().room(Room.builder().roomId(1).build()).build()));

    List<RoomScheduling> roomSchedulings =
        roomSchedulingGateway.getSchedulesByParameters(1, LocalDate.now(), LocalDate.now());

    verify(roomSchedulingRepository, times(1)).findAllByScheduledDate(LocalDate.now());
    verify(roomGateway, times(1)).findRoomById(1);
    assertThat(roomSchedulings.size()).isEqualTo(1);
  }

  @Test
  public void getSchedulesByParametersWithDifferentRoomNumbers() {
    when(roomGateway.findRoomById(3)).thenReturn(Room.builder().roomId(3).build());
    when(roomSchedulingRepository.findAllByScheduledDate(LocalDate.now()))
        .thenReturn(
            Arrays.asList(RoomScheduling.builder().room(Room.builder().roomId(5).build()).build()));

    List<RoomScheduling> roomSchedulings =
        roomSchedulingGateway.getSchedulesByParameters(3, LocalDate.now(), LocalDate.now());

    verify(roomSchedulingRepository, times(1)).findAllByScheduledDate(LocalDate.now());
    verify(roomGateway, times(1)).findRoomById(3);
    assertThat(roomSchedulings.size()).isEqualTo(0);
  }
}
