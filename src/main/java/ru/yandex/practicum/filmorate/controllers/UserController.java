package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/users")
public class UserController extends AbstractController<User, UserStorage, UserService> {

    @Autowired
    public UserController(UserService userService) {
        super(userService);
    }

    @PostMapping
    @Override
    public User create(@Valid @RequestBody User user) {
        log.info("POST: /users");
        super.create(user);
        log.info("Сведения о пользоватеме внесены, присвоен идентификатор [" + user.getId() + "].");
        return user;
    }

    @GetMapping("/{id}")
    @Override
    public User get(@PathVariable Integer id) {
        log.info("GET: /users/" + id);
        return super.get(id);
    }

    @GetMapping
    @Override
    public List<User> getAll() {
        log.info("GET: /users");
        return super.getAll();
    }

    @PutMapping
    @Override
    public User update(@Valid @RequestBody User user) {
        log.info("PUT: /users");
        super.update(user);
        log.info("Сведения о пользователе с идентификатором [" + user.getId() + "] обновлены.");
        return user;
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(Integer id) {
        log.info("DELETE: /users");
        super.delete(id);
        log.info("Пользователь с идентификатором [" + id + "] удален.");
    }

    @GetMapping("/{id}/friends")
    public Iterable<User> getFriends(@PathVariable Integer id) {
        log.info("GET: /" + id + "/friends");
        return service.getFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("PUT: /" + id + "/friends/" + friendId);
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("DELETE: /" + id + "/friends/" + friendId);
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Iterable<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("PUT: /" + id + "/friends/common/" + otherId);
        return service.getCommonFriends(id, otherId);
    }
}
