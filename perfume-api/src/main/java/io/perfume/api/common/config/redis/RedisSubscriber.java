package io.perfume.api.common.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.perfume.api.common.decorator.RedisEventSubscriber;
import io.perfume.api.notification.application.port.in.dto.NotificationResult;
import io.perfume.api.notification.application.port.in.dto.SendNotificationResult;
import io.perfume.api.notification.application.port.out.emitter.EmitterRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RedisEventSubscriber
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

  private final EmitterRepository emitterRepository;
  private final ObjectMapper objectMapper;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String userId = new String(message.getChannel());
      var notificationResult = objectMapper.readValue(message.getBody(), NotificationResult.class);

      emitterRepository
          .findAllEmitterStartWithByUserId(userId)
          .forEach(
              (key, emitter) -> {
                emitterRepository.saveEventCache(key, notificationResult);
                try {
                  emitter.send(
                      SseEmitter.event()
                          .id(key)
                          .data(
                              SendNotificationResult.from(notificationResult),
                              MediaType.APPLICATION_JSON));
                } catch (IOException e) {
                  emitterRepository.deleteEmitterByEmitterId(key);
                  emitter.completeWithError(e);
                }
              });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
