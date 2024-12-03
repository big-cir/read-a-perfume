package io.perfume.api.common.config.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.perfume.api.common.decorator.RedisEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@RedisEventPublisher
@RequiredArgsConstructor
@Slf4j
public class CircuitBreakerPublisher implements RegistryEventConsumer<CircuitBreaker> {

  private final RedisTemplate<String, Object> cacheRedisTemplate;

  @Override
  public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
    entryAddedEvent
        .getAddedEntry()
        .getEventPublisher()
        .onCallNotPermitted(event -> log.info("onCallNotPermitted {}", event))
        .onError(event -> log.error("onError {}", event))
        .onStateTransition(this::publishCircuitBreakerState);
  }

  private void publishCircuitBreakerState(CircuitBreakerOnStateTransitionEvent event) {
    if (canPublish(event)) {
      log.info(
          "Circuit breaker '{}' transitioned to OPEN state. Previous state: {}, Current state: {}",
          event.getCircuitBreakerName(),
          event.getStateTransition().getFromState(),
          event.getStateTransition().getToState());

      ChannelTopic topic = new ChannelTopic("circuit-state-change");
      cacheRedisTemplate.convertAndSend(topic.getTopic(), State.OPEN);
    }
  }

  private boolean canPublish(CircuitBreakerOnStateTransitionEvent event) {
    return event.getStateTransition().getFromState() != CircuitBreaker.State.OPEN
        && event.getStateTransition().getToState() == CircuitBreaker.State.OPEN;
  }

  @Override
  public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {}

  @Override
  public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {}
}
