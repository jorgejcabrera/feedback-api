package com.feedback.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedback.api.config.RabbitConfig;
import com.feedback.api.job.FeedbackReportJob;
import com.feedback.api.model.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMessageSender {

  private static final Logger log = LoggerFactory.getLogger(FeedbackMessageSender.class);
  private final RabbitTemplate rabbitTemplate;
  @Autowired private ObjectMapper objectMapper;

  @Autowired
  public FeedbackMessageSender(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Async
  public void sendFeedback(Feedback feedback) {
    try {
      String orderJson = objectMapper.writeValueAsString(feedback);
      Message message =
          MessageBuilder.withBody(orderJson.getBytes())
              .setContentType(MessageProperties.CONTENT_TYPE_JSON)
              .build();
      this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_FEEDBACK, message);
    } catch (Exception e) {
      log.error("It was an error while trying to send feedback message: {}", e.getMessage());
    }
  }
}
