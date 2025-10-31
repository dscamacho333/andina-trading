package co.edu.unbosque.microservice_system_management.scheduler;

import co.edu.unbosque.microservice_system_management.service.implementation.SelfPullService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class LogSelfPullScheduler {

    private final SelfPullService pull;

    @Value("${log.timezone:America/Bogota}")
    private String tz;

    @Scheduled(cron = "0 */25 * * * *", zone = "${log.timezone:America/Bogota}")
    public void snapshotEveryTwoMinutes() throws Exception {
        pull.pullSnapshot2min();
    }

    @Scheduled(cron = "0 5 0 * * *", zone = "${log.timezone:America/Bogota}")
    public void dailyClose() throws Exception {
        LocalDate yesterday = LocalDate.now(ZoneId.of(tz)).minusDays(1);
        pull.pull(yesterday);
    }
}
