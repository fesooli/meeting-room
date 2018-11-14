package br.com.fellipeoliveira.meetingroom.exceptions;

public class BusinessValidationException extends RuntimeException {

  public BusinessValidationException(String message) {
    super(message);
  }

}
