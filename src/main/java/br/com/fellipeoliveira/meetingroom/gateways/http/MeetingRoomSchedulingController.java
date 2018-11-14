package br.com.fellipeoliveira.meetingroom.gateways.http;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.fellipeoliveira.meetingroom.gateways.http.request.SchedulingDTO;
import br.com.fellipeoliveira.meetingroom.usecases.RoomSchedulingUseCase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(path = "/room", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class MeetingRoomSchedulingController {

  private final RoomSchedulingUseCase roomSchedulingUseCase;

  @RequestMapping(value = "/scheduling", method = RequestMethod.GET)
  public ResponseEntity getSchedules() {
    log.info("RECEIVED ON GET SCHEDULES METHOD");
    return ResponseEntity.ok().body(roomSchedulingUseCase.execute());
  }

  @RequestMapping(value = "/{roomId}/scheduling", method = RequestMethod.GET)
  public ResponseEntity getScheduling(
      @PathVariable("roomId") @NotNull final Integer roomId,
      @RequestParam("initialDate") @NotNull final String initialDate,
      @RequestParam("finalDate") @NotNull final String finalDate) {
    log.info(
        "RECEIVED ON GET SCHEDULING METHOD WITH PARAMETERS: {}, {}, {}",
        roomId,
        initialDate,
        finalDate);
    return ResponseEntity.ok()
        .body(
            roomSchedulingUseCase.execute(
                roomId, LocalDate.parse(initialDate), LocalDate.parse(finalDate)));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = "/scheduling", method = RequestMethod.POST)
  public ResponseEntity createScheduling(@RequestBody final SchedulingDTO schedulingDTO) {
    log.info("RECEIVED ON CREATE SCHEDULING METHOD: {}", schedulingDTO.toString());
    roomSchedulingUseCase.execute(schedulingDTO);
    return ResponseEntity.ok().body(roomSchedulingUseCase.execute());
  }

  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = "/scheduling", method = RequestMethod.PUT)
  public ResponseEntity updateScheduling(@RequestBody final SchedulingDTO schedulingDTO) {
    log.info("RECEIVED ON UPDATE SCHEDULING METHOD: {}", schedulingDTO.toString());
    roomSchedulingUseCase.execute(schedulingDTO);
    return ResponseEntity.ok().body(roomSchedulingUseCase.execute());
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/scheduling/{schedulingId}", method = RequestMethod.DELETE)
  public void deleteScheduling(@PathVariable @NotNull final Long schedulingId) {
    log.info("RECEIVED ON DELETE SCHEDULING METHOD: {}", schedulingId.toString());
    roomSchedulingUseCase.execute(schedulingId);
    log.info("Scheduling {} removed with success!", schedulingId.toString());
  }
}
