package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    void sendOffer(Integer fromUserID, Integer toUserID);

    //Подтвердить дружбу
    void confirm(Integer fromUserID, Integer toUserID);

    //Удалить дружбу или предложение дружбы
    void delete(Integer fromUserID, Integer toUserID);

    //Получить список полученных пользователем предложений дружбы
    List<Film> getReceivedOffers(Integer toUserID);

    //Получить список отправленных пользователем предложений дружбы
    List<Film> getSentOffers(Integer fromUserID);

    //Получить список друзей
    List<Film> getFriends(Integer userID);

    //Получить список общих друзей
    List<User> getCommonFriends(Integer leftUserId, Integer rightUserId);
}