package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    //Предложить дружбу/ответить на дружбу
    void addFriend(Integer fromUserID, Integer toUserID);

    //Удалить дружбу или предложение дружбы
    void deleteFriend(Integer fromUserID, Integer toUserID);

    //Получить список друзей
    List<User> getFriends(Integer userID);

    //Получить список общих друзей
    List<User> getCommonFriends(Integer leftUserId, Integer rightUserId);
}