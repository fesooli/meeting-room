package br.com.fellipeoliveira.meetingroom.gateways.http;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.usecases.RoomUseCase;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(path = "/room", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class MeetingRoomController {

  private final RoomUseCase roomUseCase;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity getRooms() {
    log.info("RECEIVED ON GET ROOM METHOD");
    return ResponseEntity.ok().body(roomUseCase.execute());
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity createRoom(@RequestBody final RoomDTO roomDTO) {
    log.info("RECEIVED ON CREATE ROOM METHOD: {}", roomDTO.toString());
    roomUseCase.execute(roomDTO);
    return ResponseEntity.ok().body(roomUseCase.execute());
  }

  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(method = RequestMethod.PUT)
  public ResponseEntity updateRoom(@RequestBody final RoomDTO roomDTO) {
    log.info("RECEIVED ON UPDATE ROOM METHOD: {}", roomDTO.toString());
    roomUseCase.execute(roomDTO);
    return ResponseEntity.ok().body(roomUseCase.execute());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/{roomId}", method = RequestMethod.DELETE)
  public void deleteRoom(@PathVariable @NotNull final Integer roomId) {
    log.info("RECEIVED ON DELETE ROOM METHOD: {}", roomId.toString());
    roomUseCase.execute(roomId);
    log.info("Room {} removed with success!", roomId.toString());
  }
}
