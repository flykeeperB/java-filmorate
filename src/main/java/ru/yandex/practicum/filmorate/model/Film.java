package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.AfterDate;

@Data
public class Film extends IdObject {

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(min = 0, max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @AfterDate(value = "27.12.1895", message = "Дата фильма не может быть раньше 28.12.1895 (день создания кино)")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
}
