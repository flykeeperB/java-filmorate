package ru.yandex.practicum.filmorate.storage.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("GenreDBStorage")
public class GenreDBStorage extends AbstractGenericDao<Genre> implements GenreStorage {

    //Кэш для ускорения обработки запросов
    final protected HashMap<Integer, Genre> cache;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "genres");
        cache = new HashMap<>();
    }

    @Override
    protected Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre result = new Genre(resultSet.getString("name"));
        result.setId(resultSet.getInt("id"));
        return result;
    }

    @Override
    protected Map<String, String> getValues(Genre genre) {
        Map<String, String> values = new HashMap<>();
        if (genre.getId() != null) {
            values.put("id", genre.getId().toString());
        }
        values.put("name", genre.getName());
        return values;
    }

    @Override
    public Genre read(Integer id) {
        validateId(id);
        if (!cache.containsKey(id)) {
            cache.put(id, super.read(id));
        }
        return cache.get(id);
    }

    @Override
    public Genre update(Genre o) {
        validateId(o.getId());
        cache.remove(o.getId());
        return super.update(o);
    }

    @Override
    public void delete(Integer id) {
        validateId(id);
        cache.remove(id);
        super.delete(id);
    }

    @Override
    public List<Genre> getGenresForFilm(Integer filmId) {
        String sql = "SELECT g.* FROM film_genres AS fg LEFT JOIN " +
                "genres as g ON fg.genre_id=g.id " +
                "WHERE fg.film_id=?";
        log.info("RUN SQL " + sql);
        return jdbcTemplate.query(sql, this::mapRow, filmId);
    }
}
