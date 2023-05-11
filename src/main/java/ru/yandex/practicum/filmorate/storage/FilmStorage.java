package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage extends Storage<Film> {
    List<Film> getPopularFilms(Integer limit);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}
