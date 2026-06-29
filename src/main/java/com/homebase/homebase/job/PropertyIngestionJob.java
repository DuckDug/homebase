package com.homebase.homebase.job;

import com.homebase.homebase.client.RealEstateApiClient;
import com.homebase.homebase.model.Property;
import com.homebase.homebase.repository.PropertyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PropertyIngestionJob {
    private final RealEstateApiClient realEstateApiClient;
    private final PropertyRepository propertyRepository;

    public PropertyIngestionJob(
            RealEstateApiClient realEstateApiClient,
            PropertyRepository propertyRepository
    ) {
        this.realEstateApiClient = realEstateApiClient;
        this.propertyRepository = propertyRepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void fetchProperties() {
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
                }
            }
        }

    }
}
