package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService extends AbstractService<Film, FilmStorage> {

    @Autowired
    public FilmService(@Qualifier("FilmDBStorage") FilmStorage storage) {
        this.storage = storage;
    }

    public void addLike(Integer filmId, Integer userId) {
        storage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        storage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer limit) {
        return storage.getPopularFilms(limit);
    }
}
