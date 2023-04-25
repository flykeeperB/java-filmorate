package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AfterDateValidator implements ConstraintValidator<AfterDate, LocalDate> {

    private LocalDate annotationDate;

    @Override
    public void initialize(AfterDate date) {
        try {
            this.annotationDate = LocalDate.parse(date.value(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            throw new ValidationException("Неверный формат даты, определяющей ограничение.");
        }
    }

    @Override
    public boolean isValid(LocalDate targetDate, ConstraintValidatorContext ctx) {
        if (targetDate == null) {
            return false;
        }
        return targetDate.isAfter(this.annotationDate);
    }
}
