package com.feedback.api.controller;

import com.feedback.api.dto.FeedbackDTO;
import com.feedback.api.model.Feedback;
import com.feedback.api.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/feedback-api")
public class FeedbackController {

  @Autowired FeedbackService feedbackService;

  @PostMapping(path = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Feedback> create(@RequestBody Feedback body) {
    return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.create(body));
  }

  @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Feedback> retrieve(@Valid @NotBlank @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(feedbackService.retrieve(id));
  }

  @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Feedback> update(
      @Valid @NotBlank @PathVariable Long id, @RequestBody FeedbackDTO body) {
    return ResponseEntity.status(HttpStatus.OK).body(feedbackService.update(id, body));
  }

  @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Feedback> delete(@Valid @NotBlank @PathVariable Long id) {
    feedbackService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(path = "/order/{orderId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Feedback> getFeedbackByOrder(@Valid @NotBlank @PathVariable Long orderId) {
    return ResponseEntity.status(HttpStatus.OK).body(feedbackService.retrieve(orderId));
  }

  @GetMapping(path = "/user/{userId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Feedback>> getFeedbacksByBuyer(
      @Valid @NotBlank @PathVariable Long userId,
      @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
      @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
      Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(feedbackService.getAllFeedbacksByBuyerIdBetween(userId, from, to, pageable));
  }

  @GetMapping(path = "/store/{storeId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Feedback>> getFeedbacksByStore(
      @Valid @NotBlank @PathVariable String storeId,
      @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
      @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
      Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(feedbackService.getAllFeedbacksByStoreIdBetween(storeId, from, to, pageable));
  }
}
