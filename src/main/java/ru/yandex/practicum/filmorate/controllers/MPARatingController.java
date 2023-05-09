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

import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MPARatingService;
import ru.yandex.practicum.filmorate.storage.MPARatingStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/mpa")
public class MPARatingController extends AbstractController<MPARating,
        MPARatingStorage, MPARatingService> {
    @Autowired
    public MPARatingController(MPARatingService service) {
        super(service);
    }

    @PostMapping
    @Override
    public MPARating create(@Valid @RequestBody MPARating mpaRating) {
        log.info("POST: /mpa");
        return super.create(mpaRating);
    }

    @GetMapping("/{id}")
    @Override
    public MPARating get(@PathVariable Integer id) {
        log.info("GET: /mpa/" + id);
        return super.get(id);
    }

    @GetMapping
    @Override
    public List<MPARating> getAll() {
        log.info("GET: /mpa");
        return super.getAll();
    }

    @PutMapping
    @Override
    public MPARating update(@Valid @RequestBody MPARating mpaRating) {
        log.info("PUT: /mpa");
        return super.update(mpaRating);
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable Integer id) {
        log.info("DELETE: /mpa");
        super.delete(id);
    }

}
