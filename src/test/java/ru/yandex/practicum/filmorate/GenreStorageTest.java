package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dataBase.GenreDBStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {
    private final GenreDBStorage genreStorage;

    @Test
    void readTest() {
        Genre genre = genreStorage.read(1);

        assertNotNull(genre, "Не получен объект жанра");
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void readAllTest() {
        List<Genre> testGenres = genreStorage.readAll();

        assertNotNull(testGenres, "Не получен список жанров");
        assertEquals(6, testGenres.size(), "Неверное количество жанров");
    }

}
