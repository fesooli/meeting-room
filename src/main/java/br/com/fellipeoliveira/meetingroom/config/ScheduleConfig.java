package br.com.fellipeoliveira.meetingroom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("schedule.config")
public class ScheduleConfig {

  private int minimumReserveTimeInMinutes;
  private int maximumReserveTimeInMinutes;
  private String scheduleStartTime;
  private String scheduleEndTime;
}
