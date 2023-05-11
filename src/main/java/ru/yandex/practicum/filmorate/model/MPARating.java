package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class MPARating extends AbstractRecord {
    @NotNull(message = "Не указано наименование позиции рейтинга MPA.")
    private String name;
    private String description;
}
