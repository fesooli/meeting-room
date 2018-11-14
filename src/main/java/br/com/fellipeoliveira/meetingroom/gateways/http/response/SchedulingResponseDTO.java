package br.com.fellipeoliveira.meetingroom.gateways.http.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SchedulingResponseDTO {

  private Long id;

  private String schedulingName;

  private LocalDateTime initialDate;

  private LocalDateTime finalDate;

  private RoomResponseDTO room;
}
