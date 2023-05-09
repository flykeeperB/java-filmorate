package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/genres")
public class GenreController extends AbstractController<Genre,
        GenreStorage, GenreService> {
    @Autowired
    public GenreController(GenreService service) {
        super(service);
    }

    @PostMapping
    @Override
    public Genre create(@Valid @RequestBody Genre genre) {
        log.info("POST: /genres");
        return super.create(genre);
    }

    @GetMapping("/{id}")
    @Override
    public Genre get(@PathVariable Integer id) {
        log.info("GET: /genres/" + id);
        return super.get(id);
    }

    @GetMapping
    @Override
    public List<Genre> getAll() {
        log.info("GET: /genres");
        return super.getAll();
    }

    @PutMapping
    @Override
    public Genre update(@Valid @RequestBody Genre genre) {
        log.info("PUT: /mpa");
        return super.update(genre);
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable Integer id) {
        log.info("DELETE: /mpa");
        super.delete(id);
    }
}
