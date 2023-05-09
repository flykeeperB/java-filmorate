package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dataBase.UserDBStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    private final UserDBStorage userStorage;

    @Test
    void createTest() {
        User user = new User(
                "testuser1@mail.ru",
                "testuser101",
                "Иванов Иван Иванович",
                LocalDate.now().minusYears(30));

        User userFilm = userStorage.create(user);

        AssertionsForClassTypes.assertThat(userFilm).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(userFilm).extracting("name").isNotNull();
    }

    @Test
    void readTest() {
        User user = userStorage.read(1);

        assertNotNull(user, "Не получена запись пользователя");
        assertThat(user).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void readAllTest() {

        List<User> beforeTestUsers = userStorage.readAll();
        assertNotNull(beforeTestUsers, "Не получен список пользователей");

        User user1 = new User(
                "testuser2@yandex.ru",
                "testuser202",
                "Сидорова Мария Сергеевна",
                LocalDate.now().minusYears(18));

        userStorage.create(user1);

        User user2 = new User(
                "testuser3@list.ru",
                "testuser303",
                "Петрова Анастасия Валерьевна",
                LocalDate.now().minusYears(23));

        userStorage.create(user2);

        List<User> testUsers = userStorage.readAll();

        assertEquals(beforeTestUsers.size() + 2, testUsers.size(), "Неверное количество пользователей");
    }

    @Test
    void updateTest() {
        User user = userStorage.read(1);
        user.setName("Джеймс Бонд");

        userStorage.update(user);

        User testUser = userStorage.read(user.getId());

        assertThat(testUser).hasFieldOrPropertyWithValue("name", "Джеймс Бонд");
    }

    @Test
    void deleteFilmTest() {

        List<User> beforeTestUsers = userStorage.readAll();

        userStorage.delete(1);

        List<User> testUsers = userStorage.readAll();

        assertEquals(beforeTestUsers.size() - 1, testUsers.size(), "Неверное количество пользователей");

        User testUser = testUsers.get(0);

        assertThat(testUser).hasFieldOrPropertyWithValue("id", 2);
    }
}
