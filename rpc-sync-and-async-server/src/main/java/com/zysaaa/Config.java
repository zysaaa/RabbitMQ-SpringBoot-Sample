package com.zysaaa;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan("com.zysaaa.service")
public class Config {

  public static final String SYNC_QUEUE_NAME = "sync_queue_name";
  public static final String ASYNC_QUEUE_NAME = "async_queue_name";
  public static final String SYNC_QUEUE_NAME_USING_MESSAGE = "sync_queue_using_message";
  public static final String ASYNC_RECEIVE_QUEUE_NAME = "async_receive_queue_name";
  public static final String ASYNC_USING_TEMPLATE_QUEUE_NAME = "async_using_template_queue_name";
  public static final String ASYNC_RECEIVE_TEMPLATE_QUEUE_NAME = "async_receive_template_queue_name";

  public static final String SYNC_EXCHANGE_NAME = "sync_exchange_name";
  public static final String ASYNC_EXCHANGE_NAME = "async_exchange_name";

  public static final String SYNC_ROUTING_KEY = "sync.routing_key";
  public static final String ASYNC_ROUTING_KEY = "async.routing_key";
  public static final String ASYNC_RECEIVE_ROUTING_KEY = "asyncreceive.routing_key";
  public static final String ASYNC_TEMPLATE_ROUTING_KEY = "asynctemplate.routing_key";



  @Bean
  public AmqpTemplate amqpTemplate(@Autowired ConnectionFactory amqpConnectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(amqpConnectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    return rabbitTemplate;
  }

  @Bean
  public Jackson2JsonMessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public Queue syncQueue() {
    return new Queue(SYNC_QUEUE_NAME); // durable, non-exclusive and non auto-delete.
  }

  @Bean
  public Queue syncQueueUsingMessage() {
    return new Queue(SYNC_QUEUE_NAME_USING_MESSAGE); // durable, non-exclusive and non auto-delete.
  }

  @Bean
  public Queue asyncQueue() {
    return new Queue(ASYNC_QUEUE_NAME); // durable, non-exclusive and non auto-delete.
  }

  @Bean
  public Queue asyncReceiveQueue() {
    return new Queue(ASYNC_RECEIVE_QUEUE_NAME); // durable, non-exclusive and non auto-delete.
  }

  @Bean
  public Queue asyncUsingTemplateQueue() {
    return new Queue(ASYNC_USING_TEMPLATE_QUEUE_NAME); // durable, non-exclusive and non auto-delete.
  }

  @Bean
  public Queue asyncReceiveUsingTemplateQueue() {
    return new Queue(ASYNC_RECEIVE_TEMPLATE_QUEUE_NAME); // durable, non-exclusive and non auto-delete.
  }
  @Bean
  public TopicExchange syncExchange() {
    return new TopicExchange(SYNC_EXCHANGE_NAME);
  }

  @Bean
  public TopicExchange asyncExchange() {
    return new TopicExchange(ASYNC_EXCHANGE_NAME);
  }

  @Bean
  public Binding binding() {
    return BindingBuilder.bind(syncQueue()).to(syncExchange()).with("sync.*");   // 转发routingkey格式为 sync.* 的信息。
  }

  @Bean
  public Binding binding2() {
    return BindingBuilder.bind(syncQueueUsingMessage()).to(syncExchange()).with("syncusingmessage.*");   // 转发routingkey格式为 syncusingmessage.* 的信息。
  }

  @Bean
  public Binding bindingAsync() {
    return BindingBuilder.bind(asyncQueue()).to(asyncExchange()).with("async.*");   // 转发routingkey格式为 async.* 的信息。
  }

  @Bean
  public Binding bindingAsyncUsingTemplate() {
    return BindingBuilder.bind(asyncUsingTemplateQueue()).to(asyncExchange()).with("asynctemplate.*");   // 转发routingkey格式为 asynctemplate.* 的信息。
  }

  @Bean
  public Binding bindingReceiveAsync() {
    return BindingBuilder.bind(asyncReceiveQueue()).to(asyncExchange()).with("asyncreceive.*");   // 转发routingkey格式为 async.* 的信息。
  }

}
