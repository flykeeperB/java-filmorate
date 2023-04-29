package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractInMemoryStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {
    @Override
    public void addFriend(Integer userId, Integer friendId) {
        //Проверяем корректность идентификаторов
        validateId(userId);
        validateId(friendId);

        if (userId.equals(friendId)) {
            throw new ValidationException("Добавление себя в свои друзья не допускается");
        }

        //Взаимно добавляем идентификаторы друзей
        read(userId).getFriends().add(friendId);
        read(friendId).getFriends().add(userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        //Проверяем корректность идентификаторов
        validateId(userId);
        validateId(friendId);

        if (userId.equals(friendId)) {
            throw new ValidationException("Добавление себя в свои друзья не допускается");
        }

        //Взаимно удаляем идентификаторы друзей
        read(userId).getFriends().remove(friendId);
        read(friendId).getFriends().remove(userId);
    }

    private List<User> getFriendsForUser(User user) {
        List<User> result = new ArrayList<>();
        if (user != null) {
            for (Integer friendId : user.getFriends()) {
                User friend = read(friendId);
                if (friend != null) {
                    result.add(friend);
                }
            }
        }
        return result;
    }

    @Override
    public List<User> getFriends(Integer userId) {
        validateId(userId);

        return getFriendsForUser(read(userId));
    }

    @Override
    public List<User> getCommonFriends(Integer leftUserId, Integer rightUserId) {
        Set<User> leftUserFriends = new HashSet<>(getFriends(leftUserId));
        Set<User> rightUserFriends = new HashSet<>(getFriends(rightUserId));

        Set<User> intersection = new HashSet<>(leftUserFriends);
        intersection.retainAll(rightUserFriends);

        return new ArrayList<>(intersection);
    }
}
