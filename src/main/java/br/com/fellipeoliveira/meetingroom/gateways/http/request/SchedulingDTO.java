package br.com.fellipeoliveira.meetingroom.gateways.http.request;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class SchedulingDTO {

  private Long id;

  @NotEmpty(message = "Scheduling Name can not be empty!")
  @NotNull(message = "Scheduling Name can not be null!")
  private String schedulingName;

  @NotNull(message = "scheduledDate can not be null!")
  private LocalDate scheduledDate;

  @NotNull(message = "scheduledTime can not be null!")
  private LocalTime scheduledTime;

  @Min(value = 0, message = "reservedTimeInMinutes can not be less than zero!")
  @NotNull(message = "reservedTimeInMinutes can not be null!")
  private Integer reservedTimeInMinutes;

  @NotNull(message = "Room can not be null!")
  private RoomDTO room;
}
