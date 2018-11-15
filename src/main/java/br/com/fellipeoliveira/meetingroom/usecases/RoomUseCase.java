package br.com.fellipeoliveira.meetingroom.usecases;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.exceptions.BusinessValidationException;
import br.com.fellipeoliveira.meetingroom.gateways.RoomGateway;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.response.RoomResponseDTO;
import br.com.fellipeoliveira.meetingroom.util.BuilderUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RoomUseCase {

  private final RoomGateway roomGateway;
  private final ValidateUseCase validateUseCase;
  private final BuilderUtil builderUtil;

  public List<RoomResponseDTO> execute() {
    return builderUtil.builderListResponse(roomGateway.listRooms());
  }

  public void execute(Integer roomId) {
    roomGateway.deleteRoom(roomId);
  }

  public void execute(RoomDTO roomDTO) {
    validateUseCase.execute(roomDTO);
    if (roomGateway.findRoomByRoomNumber(roomDTO.getRoomNumber()).isPresent()) {
      throw new BusinessValidationException("Room number is already in use!");
    }

    Room room = builderUtil.builderRoom(roomDTO);
    if(room.getRoomScheduling() == null) {
      room.setRoomScheduling(new ArrayList<>());
    }

    roomGateway.saveRoom(room);
  }

}
