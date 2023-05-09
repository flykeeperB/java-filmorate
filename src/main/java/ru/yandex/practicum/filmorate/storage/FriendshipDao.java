package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FriendshipDao {
    //Добавляет предложение дружбы
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

}
