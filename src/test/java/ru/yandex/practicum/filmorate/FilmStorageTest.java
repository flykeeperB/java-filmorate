package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dataBase.FilmDBStorage;
import ru.yandex.practicum.filmorate.storage.dataBase.MPARatingDBStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    private final FilmDBStorage filmStorage;
    private final MPARatingDBStorage mpaRatingStorage;

    @Test
    void createTest() {
        Film film = new Film(
                "Фильм",
                "Описание фильма",
                LocalDate.now().minusYears(3),
                120,
                mpaRatingStorage.read(1));

        Film testFilm = filmStorage.create(film);

        AssertionsForClassTypes.assertThat(testFilm).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(testFilm).extracting("name").isNotNull();
    }

    @Test
    void readTest() {
        Film film = filmStorage.read(1);

        assertNotNull(film, "Не получен объект фильма");
        assertThat(film).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void readAllTest() {
        Film film1 = new Film(
                "Фильм",
                "Описание фильма",
                LocalDate.now().minusYears(3),
                120,
                mpaRatingStorage.read(1));

        filmStorage.create(film1);

        Film film2 = new Film(
                "Другой фильм",
                "Описание другого фильма",
                LocalDate.now().minusYears(3),
                120,
                mpaRatingStorage.read(1));

        filmStorage.create(film2);

        List<Film> testFilms = filmStorage.readAll();

        assertEquals(2, testFilms.size(), "Неверное количество фильмов");
    }

    @Test
    void updateTest() {
        Film film = filmStorage.read(1);
        film.setName("Новый фильм");

        filmStorage.update(film);

        Film testFilm = filmStorage.read(film.getId());

        assertThat(testFilm).hasFieldOrPropertyWithValue("name", "Новый фильм");
    }

    @Test
    void deleteFilmTest() {

        List<Film> beforeTestFilms = filmStorage.readAll();

        filmStorage.delete(1);

        List<Film> testFilms = filmStorage.readAll();

        assertEquals(beforeTestFilms.size() - 1, testFilms.size(), "Неверное количество фильмов");

        Film testFilm = testFilms.get(0);

        assertThat(testFilm).hasFieldOrPropertyWithValue("id", 2);
    }
}