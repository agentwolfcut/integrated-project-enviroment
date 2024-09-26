package dev.backendintegratedproject.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import dev.backendintegratedproject.primarydatasource.repositories.StatusRepository;
import dev.backendintegratedproject.validations.annotations.StatusExists;

public class StatusExistsValidator implements ConstraintValidator<StatusExists, Integer> {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return statusRepository.existsById(integer);
    }
}
