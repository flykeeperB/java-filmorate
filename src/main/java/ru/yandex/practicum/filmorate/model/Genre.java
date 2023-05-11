package ru.yandex.practicum.filmorate.model;

import lombok.Setter;
import lombok.Getter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Genre extends AbstractRecord {
    private String name;
}
