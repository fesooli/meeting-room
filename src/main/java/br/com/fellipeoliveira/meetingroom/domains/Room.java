package br.com.fellipeoliveira.meetingroom.domains;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "ROOM")
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer roomId;
  private Integer roomNumber;
  private String roomName;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "room")
  private List<RoomScheduling> roomScheduling;

  @Override
  public String toString() {
    return "Room{" +
        "roomId=" + roomId +
        ", roomNumber=" + roomNumber +
        ", roomName='" + roomName + '\'' +
        '}';
  }
}
