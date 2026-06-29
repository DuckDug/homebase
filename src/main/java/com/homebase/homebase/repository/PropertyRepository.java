package com.homebase.homebase.repository;

import com.homebase.homebase.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    Optional<Property> findByRentcastId(String rentcastId);

    List<Property> findByCity(String city);

    List<Property> findByCityAndState(String city, String state);

    List<Property> findByZipCode(String zipCode);

    List<Property> findByPropertyType(String propertyType);

    List<Property> findByBedroomsAndCity(Integer bedrooms, String city);

    boolean existsByRentcastId(String rentcastId);
}