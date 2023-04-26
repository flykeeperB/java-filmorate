package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractInMemoryStorage;

@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {
}
