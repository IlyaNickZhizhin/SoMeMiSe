package org.smms.authorization.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Реализация валидатора {@link EnumValidator}
 */
public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

    private List<String> values;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        values = new ArrayList<>();

        final Enum[] enumValues = constraintAnnotation.enumClass().getEnumConstants();

        values = Arrays.stream(enumValues)
            .map(Enum::name)
            .map(String::toUpperCase)
            .toList();
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return values.contains(value.toUpperCase());
    } 
}



    

