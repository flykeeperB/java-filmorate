package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class User extends AbstractRecord {

    @NotBlank(message = "Не задан адрес электронной почты")
    @Email(message = "Неверный адрес электронной почты")
    private String email;

    @NotBlank(message = "Не задан логин")
    @Pattern(regexp = "\\S*$", message = "Логин содержит пробелы")
    private String login;

    private String name;

    @NotNull(message = "Не указана дата рождения пользователя")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();

    public String getName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
        return name;
    }
}
