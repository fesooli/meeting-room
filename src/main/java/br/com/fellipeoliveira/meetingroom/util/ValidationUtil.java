package br.com.fellipeoliveira.meetingroom.util;

import br.com.fellipeoliveira.meetingroom.exceptions.BusinessValidationException;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.RoomDTO;
import br.com.fellipeoliveira.meetingroom.gateways.http.request.SchedulingDTO;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidationUtil {

  private final Validator validator;

  public void validate(SchedulingDTO schedulingDTO) {
    Set<ConstraintViolation<SchedulingDTO>> violations = validator.validate(schedulingDTO);
    for (ConstraintViolation<SchedulingDTO> violation : violations) {
      throw new BusinessValidationException(violation.getMessage());
    }
  }

  public void validate(RoomDTO roomDTO) {
    Set<ConstraintViolation<RoomDTO>> violations = validator.validate(roomDTO);
    for (ConstraintViolation<RoomDTO> violation : violations) {
      throw new BusinessValidationException(violation.getMessage());
    }
  }
}
