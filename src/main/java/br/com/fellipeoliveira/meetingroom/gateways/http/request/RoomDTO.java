package br.com.fellipeoliveira.meetingroom.gateways.http.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoomDTO {

  private Integer roomId;

  @Min(value = 0, message = "roomNumber can not be less than zero!")
  @NotNull(message = "Room Number can not be null!")
  private Integer roomNumber;

  @NotEmpty(message = "Room Name can not be empty!")
  @NotNull(message = "Room Name can not be null!")
  private String roomName;
}
