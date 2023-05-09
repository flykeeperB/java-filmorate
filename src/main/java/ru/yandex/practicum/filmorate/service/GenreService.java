package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Service
public class GenreService extends AbstractService<Genre, GenreStorage> {
    @Autowired
    public GenreService(@Qualifier("GenreDBStorage") GenreStorage storage) {
        this.storage = storage;
    }
}
