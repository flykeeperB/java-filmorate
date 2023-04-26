package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    final private FilmStorage storage;
    final private UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("InMemoryFilmStorage") FilmStorage storage,
                       @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    public void checkFilmId(Integer id) {
        storage.validateId(id);
    }

    public void checkUserId(Integer id) {
        storage.validateId(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        //проверяем идентификаторы
        checkFilmId(filmId);
        checkUserId(userId);

        //Добавляем лайк
        storage.read(filmId).getLikes().add(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        //проверяем идентификаторы
        checkFilmId(filmId);
        checkUserId(userId);

        //Удаляем лайк
        storage.read(filmId).getLikes().add(userId);
    }

    public List<Film> getPopularFilms(Integer limit) {
        //Сортируем, разворачиваем, образаем по limit
        return storage
                .readAll()
                .stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
