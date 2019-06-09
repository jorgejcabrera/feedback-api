package com.feedback.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String resource, String id) {
    super(String.format("%s with id %s not found.", resource, id));
  }
}
