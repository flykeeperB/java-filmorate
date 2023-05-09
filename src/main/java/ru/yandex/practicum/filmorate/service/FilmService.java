package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService extends AbstractService<Film, FilmStorage> {
   // private final UserDao userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDBStorage") FilmStorage storage,
                       @Qualifier("UserDBStorage") UserStorage userStorage) {
        this.storage = storage;
       // this.userStorage = userStorage;
    }

    public void checkUserId(Integer id) {
    //    userStorage.validateId(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        checkUserId(userId);

        storage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        checkUserId(userId);

        storage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer limit) {
        return storage.getPopularFilms(limit);
    }
}
