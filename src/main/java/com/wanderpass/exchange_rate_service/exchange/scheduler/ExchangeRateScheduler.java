package com.wanderpass.exchange_rate_service.exchange.scheduler;

import com.wanderpass.exchange_rate_service.exchange.application.ExchangeRateSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateScheduler {

    private final ExchangeRateSyncService syncService;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private static final int MAX_CONSECUTIVE_FAILURES = 3;

    @Scheduled(cron = "${schedule.exchange-rate.cron}")
    public void syncExchangeRatesHourly() {
        String startTime = LocalDateTime.now().toString();
        log.info("=== 환율 동기화 스케줄러 시작 === {}", startTime);

        try {
            long startMillis = System.currentTimeMillis();

            syncService.syncAll();

            long duration = System.currentTimeMillis() - startMillis;
            failureCount.set(0); // 성공 시 실패 카운트 리셋

            log.info("=== 환율 동기화 성공 === 소요시간: {}ms, 완료시간: {}",
                    duration, LocalDateTime.now());

        } catch (Exception e) {
            int currentFailures = failureCount.incrementAndGet();

            log.error("=== 환율 동기화 실패 === 연속실패횟수: {}/{}, 시간: {}, 오류: {}",
                    currentFailures, MAX_CONSECUTIVE_FAILURES, LocalDateTime.now(), e.getMessage(), e);

            // 연속 실패가 임계값을 넘으면 알림 (실제 운영에서는 슬랙, 이메일 등으로 알림)
            if (currentFailures >= MAX_CONSECUTIVE_FAILURES) {
                log.error("환율 동기화 연속 실패 임계값 초과! 즉시 확인 필요!");
                // TODO: 알림 서비스 호출
            }
        }
    }

    /**
     * 애플리케이션 시작 후 1분 뒤에 한 번 실행 (초기 데이터 로드)
     */
    @Scheduled(initialDelay = 60000, fixedDelay = Long.MAX_VALUE)
    public void initialSync() {
        log.info("초기 환율 데이터 동기화 시작");
        try {
            syncService.syncAll();
            log.info("초기 환율 데이터 동기화 완료");
        } catch (Exception e) {
            log.error("초기 환율 데이터 동기화 실패: {}", e.getMessage(), e);
        }
    }
}
