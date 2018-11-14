package br.com.fellipeoliveira.meetingroom.exceptions;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
}
