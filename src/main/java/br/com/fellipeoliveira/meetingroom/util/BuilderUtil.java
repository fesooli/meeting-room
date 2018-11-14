package br.com.fellipeoliveira.meetingroom.util;

import br.com.fellipeoliveira.meetingroom.domains.Room;
import br.com.fellipeoliveira.meetingroom.domains.RoomScheduling;
import br.com.fellipeoliveira.meetingroom.exceptions.SchedulingValidationException;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.SchedulingDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.response.RoomResponseDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.response.SchedulingResponseDTO;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BuilderUtil {

  public RoomScheduling buildRoomScheduling(SchedulingDTO schedulingDTO) {
    log.info("Building RoomScheduling with this params: {}", schedulingDTO);
    return RoomScheduling.builder()
        .scheduledTime(schedulingDTO.getScheduledTime())
        .scheduledDate(schedulingDTO.getScheduledDate())
        .reservedTimeInMinutes(schedulingDTO.getReservedTimeInMinutes())
        .room(builderRoom(schedulingDTO.getRoom()))
        .schedulingName(schedulingDTO.getSchedulingName())
        .schedulingId(schedulingDTO.getId())
        .build();
  }

  public List<SchedulingResponseDTO> buildRoomSchedulesResponse(
      List<RoomScheduling> roomSchedules) {
    return roomSchedules
        .stream()
        .map(
            roomScheduling ->
                SchedulingResponseDTO.builder()
                    .initialDate(
                        LocalDateTime.of(
                            roomScheduling.getScheduledDate(), roomScheduling.getScheduledTime()))
                    .finalDate(
                        LocalDateTime.of(
                                roomScheduling.getScheduledDate(),
                                roomScheduling.getScheduledTime())
                            .plusMinutes(roomScheduling.getReservedTimeInMinutes()))
                    .room(builderRoomResponseDTO(roomScheduling.getRoom()))
                    .schedulingName(roomScheduling.getSchedulingName())
                    .id(roomScheduling.getSchedulingId())
                    .build())
        .collect(Collectors.toList());
  }

  public List<SchedulingDTO> buildRoomSchedules(List<RoomScheduling> roomSchedules) {
    return roomSchedules
        .stream()
        .map(
            roomScheduling ->
                SchedulingDTO.builder()
                    .scheduledDate(roomScheduling.getScheduledDate())
                    .scheduledTime(roomScheduling.getScheduledTime())
                    .reservedTimeInMinutes(roomScheduling.getReservedTimeInMinutes())
                    .room(builderRoomDTO(roomScheduling.getRoom()))
                    .schedulingName(roomScheduling.getSchedulingName())
                    .id(roomScheduling.getSchedulingId())
                    .build())
        .collect(Collectors.toList());
  }

  public RoomResponseDTO builderRoomResponseDTO(Room room) {
    return RoomResponseDTO.builder()
        .roomId(room.getRoomId())
        .roomNumber(room.getRoomNumber())
        .roomName(room.getRoomName())
        .build();
  }

  public RoomDTO builderRoomDTO(Room room) {
    return RoomDTO.builder()
        .roomId(room.getRoomId())
        .roomNumber(room.getRoomNumber())
        .roomName(room.getRoomName())
        .build();
  }

  public Room builderRoom(RoomDTO roomDTO) {
    log.info("Building Room with this params: {}", roomDTO);
    return Room.builder()
        .roomName(roomDTO.getRoomName())
        .roomNumber(roomDTO.getRoomNumber())
        .roomId(roomDTO.getRoomId())
        .build();
  }

  public List<RoomResponseDTO> builderListResponse(List<Room> rooms) {
    return rooms
        .stream()
        .map(
            room ->
                RoomResponseDTO.builder()
                    .roomId(room.getRoomId())
                    .roomName(room.getRoomName())
                    .roomNumber(room.getRoomNumber())
                    .build())
        .collect(Collectors.toList());
  }
}
