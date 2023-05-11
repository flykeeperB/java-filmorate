package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage extends Storage<Genre> {
    List<Genre> getGenres(Integer[] ids);
}
