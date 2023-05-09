package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validators.AfterDate;

@Builder
@Data
@AllArgsConstructor
public class Film extends AbstractRecord {

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(min = 0, max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @AfterDate(value = "27.12.1895", message = "Дата фильма не может быть раньше 28.12.1895 (день создания кино)")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Integer duration;

    private MPARating mpa;

    private final Set<Integer> likes = new HashSet<>();

    private final Set<Genre> genres = new HashSet<>();

    public void setGenres (List<Genre> genres) {
        this.genres.clear();
        if (genres!=null) {
            this.genres.addAll(genres);
        };
    }

    public void setLikes (List<Integer> likes) {
        this.likes.clear();
        if (likes!=null) {
            this.likes.addAll(likes);
        };
    }

    public Integer getLikesCount() {
        return this.likes.size();
    }
}
