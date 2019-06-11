package com.feedback.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  public static final String QUEUE_FEEDBACK = "feedback-queue";
  public static final String EXCHANGE_FEEDBACK = "feedback-exchange";

  @Bean
  Queue feedbackQueue() {
    return QueueBuilder.durable(QUEUE_FEEDBACK).build();
  }

  @Bean
  Exchange feedbackExchange() {
    return ExchangeBuilder.topicExchange(EXCHANGE_FEEDBACK).build();
  }

  @Bean
  Binding binding(Queue feedbackQueue, TopicExchange feedbackExchange) {
    return BindingBuilder.bind(feedbackQueue).to(feedbackExchange).with(QUEUE_FEEDBACK);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
    return rabbitTemplate;
  }

  @Bean
  public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
