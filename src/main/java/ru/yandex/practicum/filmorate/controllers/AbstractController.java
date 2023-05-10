package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractRecord;
import ru.yandex.practicum.filmorate.service.AbstractService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.List;

//Абстрактная снова для реализации контроллеров

public abstract class AbstractController<
        E extends AbstractRecord,
        S extends Storage<E>,
        T extends AbstractService<E, S>> {

    protected T service;

    public AbstractController(T service) {
        this.service = service;
    }

    public E create(E target) {
        return service.create(target);
    }

    public E get(Integer id) {
        validateId(id);
        return service.get(id);
    }

    public List<E> getAll() {
        return new ArrayList<>(service.getAll());
    }

    public E update(E target) {
        validateId(target.getId());
        return service.update(target);
    }

    public void delete(Integer id) {
        validateId(id);
        service.delete(id);
    }

    protected void validateId(Integer id) {
        if ((id == null) || (id < 0)) {
            throw new ValidationException("Отсутствующий или неверный идентификатор.");
        }
    }

}
