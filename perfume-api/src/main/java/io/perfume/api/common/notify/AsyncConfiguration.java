package io.perfume.api.common.notify;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

  @Bean
  public MeterRegistry meterRegistry() {
    return new SimpleMeterRegistry();
  }

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(8);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("AsyncThread-");
    executor.initialize();

    // 스레드 풀 메트릭 등록
    Gauge.builder("executor.active.threads", executor, ex -> ex.getActiveCount())
        .description("Number of active threads")
        .register(meterRegistry());

    Gauge.builder("executor.pool.size", executor, ex -> ex.getPoolSize())
        .description("Current thread pool size")
        .register(meterRegistry());

    Gauge.builder(
            "executor.queue.remaining",
            executor,
            ex -> ex.getThreadPoolExecutor().getQueue().remainingCapacity())
        .description("Remaining queue capacity")
        .register(meterRegistry());

    return executor;
  }
}
