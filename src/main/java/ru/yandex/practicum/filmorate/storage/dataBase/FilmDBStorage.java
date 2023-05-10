package ru.yandex.practicum.filmorate.storage.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

@Slf4j
@Component("FilmDBStorage")
public class FilmDBStorage extends AbstractGenericDao<Film> implements FilmStorage {

    MPARatingDBStorage mpaRatingStorage;

    GenreDBStorage genreStorage;

    @Autowired
    public FilmDBStorage(JdbcTemplate jdbcTemplate, GenreDBStorage genreStorage, MPARatingDBStorage mpaRatingStorage) {
        super(jdbcTemplate, "films");
        this.mpaRatingStorage = mpaRatingStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    protected List<String> getFields() {
        List<String> fields = new ArrayList<>();
        fields.add(getTable() + ".*");
        fields.add("ARRAY_AGG(film_genres.genre_id) as genres");
        fields.add("COUNT(film_likes.user_id) as likes");
        return fields;
    }

    @Override
    protected String getSelectSQL() {

        StringBuilder sqlBuilder = new StringBuilder(super.getSelectSQL());
        sqlBuilder.append(" LEFT JOIN film_genres ON ");
        sqlBuilder.append(getTable());
        sqlBuilder.append(".id = film_genres.film_id");
        sqlBuilder.append(" LEFT JOIN film_likes ON ");
        sqlBuilder.append(getTable());
        sqlBuilder.append(".id = film_likes.film_id");

        return sqlBuilder.toString();
    }

    @Override
    protected String getSelectGroupSQL() {
        return " GROUP BY " + getTable() + ".id";
    }

    @Override
    protected Film mapRow(ResultSet resultSet, int i) throws SQLException {
        Film result = Film.builder()
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaRatingStorage.read(resultSet.getInt("mpa_rating_id")))
                .build();

        result.setId(resultSet.getInt("id"));

        Integer[] genresIds = getIDsFromSQLResult(resultSet.getString("genres"));
        result.setGenres(genreStorage.getGenres(genresIds));

        return result;
    }

    @Override
    protected Map<String, String> getValues(Film film) {
        Map<String, String> values = new HashMap<>();
        if (film.getId() != null) {
            values.put("id", film.getId().toString());
        }
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate().toString());
        values.put("duration", film.getDuration().toString());
        if (film.getMpa() != null) {
            values.put("mpa_rating_id", film.getMpa().getId().toString());
        }
        return values;
    }

    @Override
    public Film create(Film film) {
        Set<Genre> genres = film.getGenres();
        //Запись создается впервые, поэтому связи с жанрами можно просто добавить
        Film result = super.create(film);
        for (Genre genre : genres) {
            addGenreLink(film.getId(), genre.getId());
        }
        result = this.read(result.getId());
        return result;
    }

    @Override
    public Film update(Film film) {
        validateId(film.getId());
        Set<Genre> genres = film.getGenres();
        //Запись обновляется, поэтому старые сведения о жанрах фильма удаляем
        String sql = "DELETE FROM film_genres WHERE film_id=?";
        jdbcTemplate.update(sql, film.getId());

        Film result = super.update(film);
        for (Genre genre : genres) {
            addGenreLink(film.getId(), genre.getId());
        }
        result = this.read(result.getId());
        return result;
    }

    @Override
    public List<Film> getPopularFilms(Integer limit) {
        String sql = getSelectSQL() +
                getSelectGroupSQL() +
                " ORDER BY likes DESC";

        if (limit > 0) {
            return jdbcTemplate.query(sql + " LIMIT ?", this::mapRow, limit);
        }
        return jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES  (?, ?) ";
        Integer status = jdbcTemplate.update(sql, filmId, userId);
        if (status != 0) {
            log.info("Добавлен лайк");
        } else {
            throw new ValidationException("Запись с идентификатором не запись не добавлена.");
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        Integer status = jdbcTemplate.update(sql, filmId, userId);
        if (status != 0) {
            log.info("Лайк удален");
        } else {
            throw new NotFoundException("Запись о лайке не обнаружена и не удалена.");
        }
    }

    private void addGenreLink(Integer filmId, Integer genreId) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES  (?, ?) ";
        jdbcTemplate.update(sql, filmId, genreId);
    }

}
