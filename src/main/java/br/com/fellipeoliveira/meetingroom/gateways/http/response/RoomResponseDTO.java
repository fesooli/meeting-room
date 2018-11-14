package br.com.fellipeoliveira.meetingroom.gateways.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoomResponseDTO {

  private Integer roomId;
  private Integer roomNumber;
  private String roomName;
}
