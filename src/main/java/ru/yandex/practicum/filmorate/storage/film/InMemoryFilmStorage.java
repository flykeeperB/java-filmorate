package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractInMemoryStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {

    @Override
    public void addLike(Integer filmId, Integer userId) {
        validateId(filmId);
        read(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        validateId(filmId);
        read(filmId).getLikes().add(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer limit) {
        return this
                .readAll()
                .stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
