package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/films")
public class FilmController extends AbstractController<Film> {

    private FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("InMemoryFilmStorage") FilmStorage storage,
                          FilmService filmService) {
        super(storage);
        this.storage = storage;
        this.filmService = filmService;
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST: /films");
        super.create(film);
        log.info("Сведения о фильме внесены, присвоен идентификатор [" + film.getId() + "].");
        return film;
    }

    @GetMapping("/{id}")
    @Override
    public Film get(@PathVariable Integer id) {
        log.info("GET: /films/" + id);
        return storage.read(id);
    }

    @GetMapping
    @Override
    public List<Film> getAll() {
        log.info("GET: /films");
        return super.getAll();
    }

    @PutMapping
    @Override
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT: /films");
        super.update(film);
        log.info("Сведения о фильме с идентификатором [" + film.getId() + "] обновлены.");
        return film;
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable Integer id) {
        log.info("DELETE: /films");
        super.delete(id);
        log.info("Фильм с идентификатором [" + id + "] удален.");
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET: /films/popular");
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("PUT: /films/" + id + "/like/" + userId);
        filmService.addLike(id, userId);
        log.info("Сведения о лайке под фильмом добавлены.");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("DELETE: /films/" + id + "/like/" + userId);
        filmService.deleteLike(id, userId);
        log.info("Сведения о лайке под фильмом удалены.");
    }
}
