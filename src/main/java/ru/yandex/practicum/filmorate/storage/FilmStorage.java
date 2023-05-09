package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage extends Storage<Film> {
    List<Film> getPopularFilms(Integer limit);

    void addLike(Integer film_id, Integer user_id);

    void removeLike(Integer film_id, Integer user_id);
}
