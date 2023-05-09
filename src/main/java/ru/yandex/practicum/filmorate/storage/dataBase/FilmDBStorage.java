package ru.yandex.practicum.filmorate.storage.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

@Slf4j
@Component("FilmDBStorage")
public class FilmDBStorage extends AbstractGenericDao<Film> implements FilmStorage {

    @Autowired
    @Qualifier("MPARatingDBStorage")
    MPARatingDBStorage mpaRatingStorage;

    @Autowired
    @Qualifier("GenreDBStorage")
    GenreDBStorage genreStorage;

    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "films");
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
        result.setGenres(genreStorage.getGenresForFilm(result.getId()));
        result.setLikes(getLikesByFilmId(result.getId()));
        return result;
    }

    @Override
    protected Map<String,String> getValues(Film film) {
        Map<String,String> values = new HashMap<>();
        if (film.getId()!=null) {
            values.put("id",film.getId().toString());
        }
        values.put("name",film.getName());
        values.put("description",film.getDescription());
        values.put("release_date",film.getReleaseDate().toString());
        values.put("duration",film.getDuration().toString());
        if (film.getMpa()!=null) {
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
            addGenreLink(film.getId(),genre.getId());
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
            addGenreLink(film.getId(),genre.getId());
        }
        result = this.read(result.getId());
        return result;
    }

    @Override
    public List<Film> getPopularFilms(Integer limit) {
        String sql = "SELECT films.*, c.likes_count FROM ("+
                "SELECT l.film_id as film_id, " +
                "COUNT (l.user_id) as likes_count " +
                "FROM film_likes as l " +
                "GROUP BY film_id " +
                "ORDER BY likes_count " +
                "LIMIT ?" +
                ") as c " +
                "LEFT JOIN films ON c.film_id=films.id";

        return jdbcTemplate.query(sql, this::mapRow,
                limit);
    }

    @Override
    public void addLike(Integer film_id, Integer user_id) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES  (?, ?) ";
        Integer status = jdbcTemplate.update(sql, film_id, user_id);
        if (status != 0) {
            log.info("Добавлен лайк");
        } else {
            throw new ValidationException("Запись с идентификатором не запись не добавлена.");
        }
    }

    @Override
    public void removeLike(Integer film_id, Integer user_id) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        Integer status = jdbcTemplate.update(sql, film_id, user_id);
        if (status != 0) {
            log.info("Лайк удален");
        } else {
            throw new NotFoundException("Запись о лайке не обнаружена и не удалена.");
        }
    }

    private List<Integer> getLikesByFilmId (Integer filmId) {
        String sql = "SELECT l.user_id " +
                "FROM film_likes as l " +
                "WHERE l.film_id=?";

        return jdbcTemplate.queryForList(sql,Integer.TYPE,filmId);
    }

    private void addGenreLink (Integer filmId, Integer genrId) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES  (?, ?) ";
        jdbcTemplate.update(sql, filmId, genrId);
    }


}
