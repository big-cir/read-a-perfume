package io.perfume.api.common.notify;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionMetricsAspect {

  private final Counter commitCounter;
  private final Counter rollbackCounter;
  private final Timer transactionTimer;

  public TransactionMetricsAspect(MeterRegistry registry) {
    this.commitCounter =
        Counter.builder("spring.transaction.committed")
            .description("Number of committed transactions")
            .register(registry);

    this.rollbackCounter =
        Counter.builder("spring.transaction.rollback")
            .description("Number of rolled back transactions")
            .register(registry);

    this.transactionTimer =
        Timer.builder("spring.transaction.duration")
            .description("Transaction processing time")
            .register(registry);
  }

  @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
  public Object measureTransaction(ProceedingJoinPoint joinPoint) {
    return transactionTimer.record(
        () -> {
          try {
            Object result = joinPoint.proceed();
            commitCounter.increment();
            return result;
          } catch (Throwable throwable) {
            rollbackCounter.increment();
            try {
              throw throwable;
            } catch (Throwable e) {
              throw new RuntimeException(e);
            }
          }
        });
  }
}
