package io.perfume.api.common.config.redis;

import io.perfume.api.common.decorator.RedisEventPublisher;
import io.perfume.api.notification.application.port.in.dto.NotificationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@RedisEventPublisher
@RequiredArgsConstructor
public class RedisPublisher {

  private final RedisTemplate<String, Object> redisTemplate;

  public void publish(ChannelTopic topic, String message) {
    redisTemplate.convertAndSend(topic.getTopic(), message);
  }

  public void publish(ChannelTopic topic, NotificationResult message) {
    redisTemplate.convertAndSend(topic.getTopic(), message);
  }
}
