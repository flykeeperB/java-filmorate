package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.model.AbstractRecord;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;

//Добавляем абстрактный класс, определяющий основу для реализации контроллеров

public abstract class AbstractController<T extends AbstractRecord> {

    protected Storage<T> storage;

    public AbstractController(Storage<T> storage) {
        this.storage = storage;
    }

    public T create(T target) {
        storage.create(target);
        return target;
    }

    public T get(Integer id) {
        return storage.read(id);
    }

    public List<T> getAll() {
        return new ArrayList<T>(storage.readAll());
    }

    public T update(T target) {
        return storage.update(target);
    }

    public void delete(Integer id) {
        storage.delete(id);
    }

}
