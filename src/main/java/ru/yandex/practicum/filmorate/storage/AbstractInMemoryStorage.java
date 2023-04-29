package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractInMemoryStorage<T extends AbstractRecord> implements Storage<T> {
    protected Map<Integer, T> storage = new HashMap<>();

    protected Integer lastId = 0;

    protected Integer createId() {
        return ++lastId;
    }

    public void validateId(Integer id) {
        if (id == null) {
            throw new ValidationException("Идентификатор не задан!");
        }
        if (!storage.containsKey(id)) {
            throw new NotFoundException("Запись с идентификатором [" + id + "] не найдена!");
        }
    }

    public List<T> readAll() {
        return new ArrayList<>(storage.values());
    }

    public T create(T target) {
        target.setId(createId());
        storage.put(target.getId(), target);
        return target;
    }

    public T read(Integer id) {
        validateId(id);
        return storage.get(id);
    }

    public T update(T target) {
        validateId(target.getId());
        storage.put(target.getId(), target);
        return target;
    }

    public void delete(Integer id) {
        validateId(id);
        storage.remove(id);
    }
}
