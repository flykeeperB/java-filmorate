package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingStorage;

@Service
public class MPARatingService extends AbstractService<MPARating, MPARatingStorage> {
    @Autowired
    public MPARatingService(@Qualifier("MPARatingDBStorage") MPARatingStorage storage) {
        this.storage = storage;
    }
}
