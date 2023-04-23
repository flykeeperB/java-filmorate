package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/films")
public class FilmController extends Controller<Film> {

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST: /films");
        super.create(film);
        log.info("Сведения о фильме внесены, присвоен идентификатор ["+film.getId()+"].");
        return film;
    }

    @GetMapping
    @Override
    public List<Film> read() {
        log.info("GET: /films");
        return super.read();
    }

    @PutMapping
    @Override
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT: /films");
        super.update(film);
        log.info("Сведения о фильме с идентификатором [" + film.getId() + "] обновлены.");
        return film;
    }

    @DeleteMapping
    @Override
    public void delete(Integer id) {
        log.info("DELETE: /films");
        super.delete(id);
        log.info("Фильм с идентификатором [" + id + "] удален.");
    }
}
