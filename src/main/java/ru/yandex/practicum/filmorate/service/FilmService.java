package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService extends AbstractService<Film, FilmStorage> {
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("InMemoryFilmStorage") FilmStorage storage,
                       @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    public void checkUserId(Integer id) {
        userStorage.validateId(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        checkUserId(userId);

        storage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        checkUserId(userId);

        storage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer limit) {
        return storage.getPopularFilms(limit);
    }
}
