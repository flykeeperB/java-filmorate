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
        assertEquals(5, testMPARatings.size(), "Неверное количество рейтинговых позиций");
    }
}
