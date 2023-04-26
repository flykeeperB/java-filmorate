package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class UserService {
    final String SELFFRIENDING_NOT_ALLOWED = "Добавление себя в свои друзья не допускается";

    final private UserStorage storage;

    @Autowired
    public UserService(@Qualifier("InMemoryUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    public void checkUserId(Integer id) {
        storage.validateId(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        //Проверяем корректность идентификаторов
        checkUserId(userId);
        checkUserId(friendId);

        if (userId.equals(friendId)) {
            throw new ValidationException(SELFFRIENDING_NOT_ALLOWED);
        }

        //Взаимно добавляем идентификаторы друзей
        storage.read(userId).getFriends().add(friendId);
        storage.read(friendId).getFriends().add(userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        //Проверяем корректность идентификаторов
        checkUserId(userId);
        checkUserId(friendId);
        if (userId.equals(friendId)) {
            throw new ValidationException(SELFFRIENDING_NOT_ALLOWED);
        }

        //Взаимно удаляем идентификаторы друзей
        storage.read(userId).getFriends().remove(friendId);
        storage.read(friendId).getFriends().remove(userId);
    }

    private List<User> getFriendsForUser(User user) {
        List<User> result = new ArrayList<>();
        if (user != null) {
            for (Integer friendId : user.getFriends()) {
                User friend = storage.read(friendId);
                if (friend != null) {
                    result.add(friend);
                }
            }
        }
        return result;
    }

    public List<User> getFriends(Integer userId) {
        //Проверяем корректность идентификатора
        checkUserId(userId);

        //Получаем список друзей
        return getFriendsForUser(storage.read(userId));
    }

    public List<User> getCommonFriends(Integer leftUserId, Integer rightUserId) {
        Set<User> leftUserFriends = new HashSet<>(getFriends(leftUserId));
        Set<User> rightUserFriends = new HashSet<>(getFriends(rightUserId));

        Set<User> intersection = new HashSet<>(leftUserFriends);
        intersection.retainAll(rightUserFriends);

        return new ArrayList<>(intersection);
    }
}
