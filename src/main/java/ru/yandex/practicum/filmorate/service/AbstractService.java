package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.AbstractRecord;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class AbstractService<E extends AbstractRecord, T extends Storage<E>> {
    protected T storage;

    public E create(E e) {
        return storage.create(e);
    }

    public E get(Integer id) {
        return storage.read(id);
    }

    public List<E> getAll() {
        return new ArrayList<>(storage.readAll());
    }

    public E update(E target) {
        return storage.update(target);
    }

    public void delete(Integer id) {
        storage.delete(id);
    }

}
