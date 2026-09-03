package com.atmosware.internship_project_tmt.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class StoryPointValidator implements ConstraintValidator<ValidStoryPoint, Integer> {

    private final List<Integer> validPoints = Arrays.asList(1, 2, 3, 5, 8, 13);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // @NotNull'a gider
        if (value == null) {
            return true;
        }

        // validPoints içinde mi?
        return validPoints.contains(value);
    }
}