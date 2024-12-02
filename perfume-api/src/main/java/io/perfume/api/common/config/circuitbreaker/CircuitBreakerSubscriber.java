package io.perfume.api.common.config.circuitbreaker;

import io.perfume.api.common.decorator.RedisEventSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@RedisEventSubscriber
public class CircuitBreakerSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
