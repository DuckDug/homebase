package com.homebase.homebase.job;

import com.homebase.homebase.client.RealEstateApiClient;
import com.homebase.homebase.model.JobLog;
import com.homebase.homebase.model.Property;
import com.homebase.homebase.repository.JobLogRepository;
import com.homebase.homebase.repository.PropertyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PropertyIngestionJob {
    private final RealEstateApiClient realEstateApiClient;
    private final PropertyRepository propertyRepository;
    private final JobLogRepository jobLogRepository;
    private static final String JOBNAME = "PropertyIngestionJob";

    public PropertyIngestionJob(
            RealEstateApiClient realEstateApiClient,
            PropertyRepository propertyRepository,
            JobLogRepository jobLogRepository
    ) {
        this.realEstateApiClient = realEstateApiClient;
        this.propertyRepository = propertyRepository;
        this.jobLogRepository = jobLogRepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void fetchProperties() {
        LocalDateTime start = LocalDateTime.now();
        String status = "SUCCESS";
        String errorMessage = null;
        int recordsProcessed = 0;

        try {
            List<String> cities = List.of(
                    "Austin",
                    "Dallas",
                    "San Antonio",
                    "Houston"
            );

            for (String city : cities) {
                List<Property> propertiesList = realEstateApiClient.fetchPropertiesByCity(city, "TX", 10);
                for (Property property : propertiesList) {

                    if (!propertyRepository.existsByRentcastId(property.getRentcastId())) {
                        propertyRepository.save(property);
                        recordsProcessed++;
                    }
                }
            }
        } catch (Exception e) {
            status = "FAILED";
            errorMessage = e.getMessage();
        } finally {
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
}
