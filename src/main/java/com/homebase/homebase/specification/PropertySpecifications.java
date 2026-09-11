package com.homebase.homebase.specification;

import com.homebase.homebase.model.Property;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecifications {

    public static Specification<Property> hasCity(String city) {
        if (city == null) {
            return null;
        }
        return (root, query, cb) ->  cb.equal(root.get("city"), city);
    }

    public static Specification<Property> hasState(String state) {
        if (state == null) {
            return null;
        }
        return (root, query, cb) ->  cb.equal(root.get("state"), state);
    }

    public static Specification<Property> hasMinBedrooms(Integer bedrooms) {
        if (bedrooms == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("bedrooms"), bedrooms);
    }

    public static Specification<Property> hasPropertyType(String propertyType) {
        if (propertyType == null) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.get("propertyType"), propertyType);
    }
}
