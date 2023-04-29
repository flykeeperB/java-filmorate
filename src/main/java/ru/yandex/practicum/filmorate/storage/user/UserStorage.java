package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface UserStorage extends Storage<User> {

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getFriends(Integer userId);

    List<User> getCommonFriends(Integer leftUserId, Integer rightUserId);
}
