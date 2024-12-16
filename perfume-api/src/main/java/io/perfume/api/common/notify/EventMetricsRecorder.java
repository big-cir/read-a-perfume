package io.perfume.api.common.notify;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class EventMetricsRecorder {

  private final Counter eventProcessedCounter;
  private final Timer eventProcessingTimer;

  public EventMetricsRecorder(MeterRegistry registry) {
    this.eventProcessedCounter =
        Counter.builder("application.event.processed.count")
            .description("Total number of events processed")
            .register(registry);

    this.eventProcessingTimer =
        Timer.builder("application.event.processing.time")
            .description("Time taken to process events")
            .register(registry);
  }

  public void recordEventProcessing(Runnable eventHandler) {
    eventProcessedCounter.increment();
    eventProcessingTimer.record(eventHandler);
  }
}
