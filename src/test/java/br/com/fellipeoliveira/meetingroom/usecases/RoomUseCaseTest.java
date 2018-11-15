package br.com.fellipeoliveira.meetingroom.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.exceptions.BusinessValidationException;
import br.com.fellipeoliveira.meetingroom.gateways.RoomGateway;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.response.RoomResponseDTO;
import br.com.fellipeoliveira.meetingroom.util.BuilderUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoomUseCaseTest {

  @InjectMocks private RoomUseCase roomUseCase;

  @Mock private RoomGateway roomGateway;

  @Mock private ValidateUseCase validateUseCase;

  @Mock private BuilderUtil builderUtil;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void findRooms() {
    when(builderUtil.builderListResponse(any())).thenReturn(Arrays.asList(new RoomResponseDTO()));

    final List<RoomResponseDTO> roomResponses = roomUseCase.execute();

    verify(roomGateway, times(1)).listRooms();
    assertThat(roomResponses.size()).isEqualTo(1);
  }

  @Test
  public void deleteRoom() {
    roomUseCase.execute(1);

    verify(roomGateway, times(1)).deleteRoom(1);
  }

  @Test
  public void saveRoom() {
    final RoomDTO roomDTO = getRoomDTO();
    when(builderUtil.builderRoom(any())).thenReturn(getRoom(roomDTO));

    roomUseCase.execute(roomDTO);

    verify(validateUseCase, times(1)).execute(roomDTO);
    verify(builderUtil, times(1)).builderRoom(roomDTO);
    verify(roomGateway, times(1)).saveRoom(builderUtil.builderRoom(roomDTO));
  }

  @Test
  public void saveRoomWithNonNullSchedule() {
    final RoomDTO roomDTO = getRoomDTO();
    Room room = getRoom(roomDTO);
    room.setRoomScheduling(new ArrayList<>());
    when(builderUtil.builderRoom(any())).thenReturn(room);

    roomUseCase.execute(roomDTO);

    verify(validateUseCase, times(1)).execute(roomDTO);
    verify(builderUtil, times(1)).builderRoom(roomDTO);
    verify(roomGateway, times(1)).saveRoom(builderUtil.builderRoom(roomDTO));
  }

  @Test(expected = BusinessValidationException.class)
  public void saveRoomError() {
    final RoomDTO roomDTO = getRoomDTO();
    when(roomGateway.findRoomByRoomNumber(any())).thenReturn(Optional.of(new Room()));

    roomUseCase.execute(roomDTO);

    verify(validateUseCase, times(1)).execute(roomDTO);
    verify(builderUtil, times(1)).builderRoom(roomDTO);
    verify(roomGateway, times(1)).saveRoom(builderUtil.builderRoom(roomDTO));
  }

  private RoomDTO getRoomDTO() {
    return RoomDTO.builder().roomName("Sala 01").roomNumber(1).roomId(1).build();
  }

  private Room getRoom(RoomDTO roomDTO) {
    return Room.builder()
        .roomName(roomDTO.getRoomName())
        .roomNumber(roomDTO.getRoomNumber())
        .roomId(roomDTO.getRoomId())
        .build();
  }
}
