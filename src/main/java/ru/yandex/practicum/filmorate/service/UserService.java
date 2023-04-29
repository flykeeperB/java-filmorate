package ru.yandex.practicum.filmorate.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class UserService extends AbstractService<User, UserStorage> {

    @Autowired
    public UserService(@Qualifier("InMemoryUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        storage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        storage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        return storage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer leftUserId, Integer rightUserId) {
        return storage.getCommonFriends(leftUserId, rightUserId);
    }
}
