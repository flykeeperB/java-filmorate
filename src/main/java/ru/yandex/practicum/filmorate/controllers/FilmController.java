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
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/films")
public class FilmController extends AbstractController<Film, FilmStorage, FilmService> {

    @Autowired
    public FilmController(FilmService service) {
        super(service);
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST: /films");
        return super.create(film);
    }

    @GetMapping("/{id}")
    @Override
    public Film get(@PathVariable Integer id) {
        log.info("GET: /films/" + id);
        return super.get(id);
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
        return super.update(film);
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable Integer id) {
        log.info("DELETE: /films");
        super.delete(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "-1") int count) {
        log.info("GET: /films/popular");
        return service.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("PUT: /films/" + id + "/like/" + userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("DELETE: /films/" + id + "/like/" + userId);
        service.deleteLike(id, userId);
    }
}
