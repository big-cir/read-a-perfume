package io.perfume.api.common.config.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.perfume.api.common.decorator.RedisEventSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@RedisEventSubscriber
@Slf4j
public class CircuitBreakerSubscriber implements MessageListener {

  private final CircuitBreakerRegistry circuitBreakerRegistry;
  private final RedisTemplate<?, ?> redisTemplate;

  public CircuitBreakerSubscriber(
      CircuitBreakerRegistry circuitBreakerRegistry,
      @Qualifier("RedisCacheMessageListener")
          RedisMessageListenerContainer redisMessageListenerContainer,
      @Qualifier("cacheRedisTemplate") RedisTemplate<?, ?> redisTemplate) {
    this.circuitBreakerRegistry = circuitBreakerRegistry;
    this.redisTemplate = redisTemplate;

    ChannelTopic topic = new ChannelTopic("circuit-state-change");
    redisMessageListenerContainer.addMessageListener(this, topic);
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String receivedStateString =
          (String) redisTemplate.getValueSerializer().deserialize(message.getBody());
      State receivedState = State.valueOf(receivedStateString);

      if (receivedState == State.OPEN) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("CB-redis");
        circuitBreaker.transitionToOpenState();
        log.info("Circuit Breaker forcibly set to OPEN state by Redis message");
      }
    } catch (Exception e) {
      log.error("Error processing circuit breaker state message", e);
    }
  }
}
