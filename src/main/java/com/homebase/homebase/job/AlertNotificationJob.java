package com.homebase.homebase.job;

import com.homebase.homebase.client.EmailClient;
import com.homebase.homebase.model.JobLog;
import com.homebase.homebase.model.PriceAlert;
import com.homebase.homebase.model.PriceAlertStatus;
import com.homebase.homebase.repository.JobLogRepository;
import com.homebase.homebase.repository.PriceAlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class AlertNotificationJob {
    private final PriceAlertRepository priceAlertRepository;
    private final JobLogRepository jobLogRepository;
    private final EmailClient emailClient;
    private static final String JOBNAME = "AlertNotificationJob";

    public AlertNotificationJob(
            PriceAlertRepository priceAlertRepository,
            JobLogRepository jobLogRepository,
            EmailClient emailClient
    ) {
        this.priceAlertRepository = priceAlertRepository;
        this.jobLogRepository = jobLogRepository;
        this.emailClient = emailClient;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void runAlertNotificationJob() {
        LocalDateTime start = LocalDateTime.now();
        String status = "SUCCESS";
        String errorMessage = null;
        int recordsProcessed = 0;
        int failureCount = 0;

        try {
            List<PriceAlert> triggeredPriceAlerts = priceAlertRepository.findByStatusAndNotifiedAtIsNull(PriceAlertStatus.TRIGGERED);

            for (PriceAlert priceAlert : triggeredPriceAlerts) {
                try {
                    String subject = buildSubject(priceAlert);
                    String body = buildBody(priceAlert);
                    emailClient.sendAlertEmail(subject, body);
                    priceAlert.setNotifiedAt(LocalDateTime.now());
                    priceAlertRepository.save(priceAlert);
                    recordsProcessed++;
                } catch (Exception e) {
                    failureCount++;
                    log.warn("Failed to send alert email for PriceAlert id={}: {}", priceAlert.getId(), e.getMessage());
                }

            }

            if (failureCount > 0) {
                status = "PARTIAL_FAILURE";
                errorMessage = failureCount + " of " + triggeredPriceAlerts.size() + " emails failed to send";
            }

        }
        catch (Exception e) {
            status = "FAILED";
            errorMessage = e.getMessage();
        }
        finally {
            LocalDateTime end = LocalDateTime.now();

            JobLog.JobLogBuilder builder = JobLog.builder()
                    .jobName(JOBNAME)
                    .startedAt(start)
                    .finishedAt(end)
                    .status(status)
                    .errorMessage(errorMessage)
                    .recordsProcessed(recordsProcessed);

            jobLogRepository.save(builder.build());
        }
    }

    private String buildSubject(PriceAlert alert) {
        return "Price alert triggered: " + alert.getSymbol();
    }

    private String buildBody(PriceAlert alert) {
        return "<h3>Price alert triggered</h3>"
                + "<p>" + alert.getSymbol() + " has crossed your target of $" + alert.getTargetPrice()
                + " (" + alert.getCondition() + ").</p>";
    }

}
