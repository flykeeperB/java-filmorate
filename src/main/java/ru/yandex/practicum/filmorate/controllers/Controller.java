package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.IdObject;

import java.util.*;

//Добавляем абстрактный класс, определяющий основу для реализации CRUD в контроллерах

public abstract class Controller<T extends IdObject> {

    protected Map<Integer, T> storage = new HashMap<>();

    protected int lastId = 0;

    protected int createId() {
        return ++lastId;
    }

    public T create(@Valid @RequestBody T target) {
        target.setId(createId());
        storage.put(target.getId(), target);
        return target;
    }

    public List<T> read() {
        return new ArrayList<T>(storage.values());
    }

    public T update(@Valid @RequestBody T target) {
        if (target.getId() == null) {
            throw new ValidationException("Идентификатор не задан!");
        }
        if (!storage.containsKey(target.getId())) {
            throw new FilmNotFoundException("Запись с идентификатором [" + target.getId() + "] не найдена!");
        }
        storage.put(target.getId(), target);
        return target;
    }

    public void delete(Integer id) {
        if (id == null) {
            throw new ValidationException("Идентификатор не задан!");
        }
        if (storage.remove(id) == null) {
            throw new FilmNotFoundException("Запись с идентификатором [" + id + "] не найдена!");
        }
        storage.remove(id);
    }

}
