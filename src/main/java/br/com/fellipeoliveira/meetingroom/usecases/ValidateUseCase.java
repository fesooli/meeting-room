package br.com.fellipeoliveira.meetingroom.usecases;

import br.com.fellipeoliveira.meetingroom.config.ScheduleConfig;
import br.com.fellipeoliveira.meetingroom.exceptions.SchedulingValidationException;
import br.com.fellipeoliveira.meetingroom.gateways.RoomSchedulingGateway;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.SchedulingDTO;
import br.com.fellipeoliveira.meetingroom.util.BuilderUtil;
import br.com.fellipeoliveira.meetingroom.util.ValidationUtil;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidateUseCase {

  private final RoomSchedulingGateway roomSchedulingGateway;
  private final ValidationUtil validationUtil;
  private final BuilderUtil builderUtil;
  private final ScheduleConfig scheduleConfig;

  public void execute(SchedulingDTO schedulingDTO) {
    final LocalDateTime now = LocalDateTime.now();
    final LocalDateTime schedulePeriod =
        LocalDateTime.of(schedulingDTO.getScheduledDate(), schedulingDTO.getScheduledTime());

    if (schedulePeriod.isBefore(now)) {
      throw new SchedulingValidationException("The schedule can not be earlier than now!");
    }

    log.info("Starting schedule validation");

    validationUtil.validate(schedulingDTO);
    log.info("Verifying verifyIfScheduleIsInTheAllowedRange");
    verifyIfScheduleIsInTheAllowedRange(schedulingDTO);

    log.info("Verifying verifyIfScheduleIsLessThanTheMinimumTime");
    verifyIfScheduleIsLessThanTheMinimumTime(schedulingDTO);

    log.info("Verifying verifyIfScheduleIsGreaterThanTheMaximumTime");
    verifyIfScheduleIsGreaterThanTheMaximumTime(schedulingDTO);

    log.info("Verifying verifyIfScheduleAlreadyExists");
    verifyIfScheduleAlreadyExists(schedulingDTO);

    log.info("Verifying verifyIfDateIsWeekend");
    verifyIfDateIsWeekend(schedulingDTO);
  }

  public void execute(RoomDTO roomDTO) {
    validationUtil.validate(roomDTO);
  }

  private void verifyIfScheduleIsInTheAllowedRange(SchedulingDTO schedulingDTO) {
    final LocalDateTime scheduledDate =
        LocalDateTime.of(schedulingDTO.getScheduledDate(), schedulingDTO.getScheduledTime());
    final LocalDateTime scheduledDateFinal =
        LocalDateTime.of(
            schedulingDTO.getScheduledDate(),
            schedulingDTO.getScheduledTime().plusMinutes(schedulingDTO.getReservedTimeInMinutes()));

    final LocalTime startTime = LocalTime.parse(scheduleConfig.getScheduleStartTime());
    final LocalTime endTime = LocalTime.parse(scheduleConfig.getScheduleEndTime());

    if ((!isBetweenToDateTime(
            scheduledDate.toLocalTime(), startTime, endTime)
        && !isBetweenToDateTime(
            scheduledDateFinal.toLocalTime(), startTime, endTime))) {
      throw new SchedulingValidationException(
          "You can not schedule this room outside the allowed hour range. The time range is "
              .concat(scheduleConfig.getScheduleStartTime())
              .concat(" and ")
              .concat(scheduleConfig.getScheduleEndTime()));
    }
  }

  private void verifyIfDateIsWeekend(SchedulingDTO schedulingDTO) {
    LocalDateTime initialDate =
        LocalDateTime.of(schedulingDTO.getScheduledDate(), schedulingDTO.getScheduledTime());

    LocalDateTime finalDate =
        LocalDateTime.of(schedulingDTO.getScheduledDate(), schedulingDTO.getScheduledTime())
            .plusMinutes(schedulingDTO.getReservedTimeInMinutes());

    LocalDateTime now = initialDate;
    for (int day = initialDate.getDayOfMonth(); day == finalDate.getDayOfMonth(); day++) {
      if (DayOfWeek.SATURDAY == now.getDayOfWeek()) {
        throw new SchedulingValidationException(
            "You can not schedule a start or end date on the weekend!");
      } else if (DayOfWeek.SUNDAY == now.getDayOfWeek()) {
        throw new SchedulingValidationException(
            "You can not schedule a start or end date on the weekend!");
      }
      now = now.plusDays(day);
    }
  }

  private void verifyIfScheduleIsLessThanTheMinimumTime(SchedulingDTO schedulingDTO) {
    if (schedulingDTO.getReservedTimeInMinutes()
        < scheduleConfig.getMinimumReserveTimeInMinutes()) {
      throw new SchedulingValidationException(
          "The minimum time to reserve is "
              .concat(String.valueOf(scheduleConfig.getMinimumReserveTimeInMinutes()))
              .concat(" minutes!"));
    }
  }

  private void verifyIfScheduleIsGreaterThanTheMaximumTime(SchedulingDTO schedulingDTO) {
    if (schedulingDTO.getReservedTimeInMinutes()
        > scheduleConfig.getMaximumReserveTimeInMinutes()) {
      throw new SchedulingValidationException(
          "The maximum time to reserve is "
              .concat(String.valueOf(scheduleConfig.getMaximumReserveTimeInMinutes()))
              .concat(" minutes!"));
    }
  }

  private void verifyIfScheduleAlreadyExists(SchedulingDTO schedulingDTO) {
    final List<SchedulingDTO> schedules =
        builderUtil.buildRoomSchedules(
            roomSchedulingGateway.getSchedulesByParameters(
                schedulingDTO.getRoom().getRoomId(), schedulingDTO.getScheduledDate(), null));

    final LocalDateTime scheduledDateWithTime =
        LocalDateTime.of(schedulingDTO.getScheduledDate(), schedulingDTO.getScheduledTime());
    final LocalDateTime scheduledDateWithFinalTime =
        LocalDateTime.of(
            schedulingDTO.getScheduledDate(),
            schedulingDTO.getScheduledTime().plusMinutes(schedulingDTO.getReservedTimeInMinutes()));

    if (!schedules.isEmpty()) {
      schedules.forEach(
          schedule -> {
            final LocalDateTime scheduledDate =
                LocalDateTime.of(schedule.getScheduledDate(), schedule.getScheduledTime());
            final LocalDateTime scheduledDateFinal =
                LocalDateTime.of(
                    schedule.getScheduledDate(),
                    schedule.getScheduledTime().plusMinutes(schedule.getReservedTimeInMinutes()));

            if ((isBetweenToDates(scheduledDate, scheduledDateWithTime, scheduledDateWithFinalTime)
                    || isBetweenToDates(
                        scheduledDateFinal, scheduledDateWithTime, scheduledDateWithFinalTime))
                || (isBetweenToDates(scheduledDateWithTime, scheduledDate, scheduledDateFinal)
                    || isBetweenToDates(
                        scheduledDateWithFinalTime, scheduledDate, scheduledDateFinal))) {
              throw new SchedulingValidationException(
                  "There is already a schedule for this period!");
            }

            if (scheduledDateWithTime.isEqual(scheduledDate)
                && scheduledDateWithFinalTime.isEqual(scheduledDateFinal)) {
              throw new SchedulingValidationException(
                  "There is already a schedule for this period!");
            }
          });
    }
  }

  private boolean isBetweenToDates(
      LocalDateTime dateToCompare, LocalDateTime dateOne, LocalDateTime dateTwo) {
    log.info(
        "Comparing Dates -> Date to Compare: {}, Date One {}, Date Two {}",
        dateToCompare,
        dateOne,
        dateTwo);
    return dateToCompare.isAfter(dateOne) && dateToCompare.isBefore(dateTwo);
  }

  private boolean isBetweenToDateTime(
      LocalTime timeToCompare, LocalTime timeOne, LocalTime timeTwo) {
    log.info(
        "Comparing times -> Time to Compare: {}, Time One {}, Time Two {}",
        timeToCompare,
        timeOne,
        timeTwo);
    return (timeToCompare.isAfter(timeOne) && timeToCompare.isBefore(timeTwo));
  }
}
