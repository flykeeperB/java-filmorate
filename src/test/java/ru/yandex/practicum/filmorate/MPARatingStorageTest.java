package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.dataBase.MPARatingDBStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPARatingStorageTest {
    private final MPARatingDBStorage mpaRatingStorage;

    @Test
    void readTest() {
        MPARating mpaRating = mpaRatingStorage.read(1);

        assertNotNull(mpaRating, "Не получен объект ретинга");
        assertThat(mpaRating).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void readAllTest() {
        List<MPARating> testMPARatings = mpaRatingStorage.readAll();

        assertNotNull(testMPARatings, "Не получен список рейтинговых позиций");
        assertEquals(6, testMPARatings.size(), "Неверное количество рейтинговых позиций");
    }

    @Test
    void updateTest() {
        MPARating mpaRating = mpaRatingStorage.read(1);
        mpaRating.setName("Новая позиция рейтинга");

        mpaRatingStorage.update(mpaRating);

        MPARating testMPARating = mpaRatingStorage.read(mpaRating.getId());

        assertThat(testMPARating).hasFieldOrPropertyWithValue("name", "Совершенно новый жанр");
    }

    @Test
    void deleteGenreTest() {
        List<MPARating> beforeTestMPARatings = mpaRatingStorage.readAll();

        mpaRatingStorage.delete(1);

        List<MPARating> testMPARatings = mpaRatingStorage.readAll();

        assertEquals(beforeTestMPARatings.size() - 1, testMPARatings.size(), "Неверное количество позиций рейтинга");

        MPARating testMPARating = testMPARatings.get(0);

        assertThat(testMPARating).hasFieldOrPropertyWithValue("id", 2);
    }
}
