package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AbstractRecord;

import java.util.List;

public interface Storage<T extends AbstractRecord> {
    List<T> readAll();

    T create(T o);

    T read(Integer id);

    T update(T o);

    void delete(Integer id);

    void validateId(Integer id);
}
