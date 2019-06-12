package com.feedback.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HealthController {
  private final AtomicLong counter = new AtomicLong();

  @RequestMapping("/status")
  public ResponseEntity echo() {
    return ResponseEntity.status(HttpStatus.OK).body(counter.incrementAndGet());
  }
}
