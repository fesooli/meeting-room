package br.com.fellipeoliveira.meetingroom.domains;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "ROOM_SCHEDULING")
public class RoomScheduling {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long schedulingId;

  private String schedulingName;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "room_id")
  private Room room;

  private Integer reservedTimeInMinutes;

  private LocalDate scheduledDate;

  private LocalTime scheduledTime;
}
