package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/users")
public class UserController extends Controller<User> {

    @GetMapping
    @Override
    public List<User> read() {
        log.info("GET: /users");
        return super.read();
    }

    @PostMapping
    @Override
    public User create(@Valid @RequestBody User user) {
        log.info("POST: /users");
        super.create(user);
        log.info("Сведения о пользоватеме внесены, присвоен идентификатор [" + user.getId() + "].");
        return user;
    }

    @PutMapping
    @Override
    public User update(@Valid @RequestBody User user) {
        log.info("PUT: /users");
        super.update(user);
        log.info("Сведения о пользователе с идентификатором [" + user.getId() + "] обновлены.");
        return user;
    }

    @DeleteMapping
    @Override
    public void delete(Integer id) {
        log.info("DELETE: /users");
        super.delete(id);
        log.info("Пользователь с идентификатором [" + id + "] удален.");
    }
}
