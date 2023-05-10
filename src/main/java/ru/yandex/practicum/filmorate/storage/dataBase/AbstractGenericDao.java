package ru.yandex.practicum.filmorate.storage.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractRecord;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public abstract class AbstractGenericDao<T extends AbstractRecord> implements Storage<T> {

    @NotNull
    protected final JdbcTemplate jdbcTemplate;

    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @NotBlank
    private final String table;

    public AbstractGenericDao(JdbcTemplate jdbcTemplate, String table) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.table = table;
    }

    protected String getTable() {
        return this.table;
    }

    protected List<String> getFields() {
        return new ArrayList<>(Arrays.asList("*"));
    }

    abstract T mapRow(ResultSet resultSet, int i) throws SQLException;

    //Возвращает набор значений полей (key=>value) для формирования запроса на добавление/обновление
    protected abstract Map<String, String> getValues(T t);

    protected String getSelectSQL() {
        List<String> fields = getFields();

        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        sqlBuilder.append(String.join(", ", fields));
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(getTable());

        return sqlBuilder.toString();
    }

    protected String getSelectGroupSQL() {
        return "";
    }

    protected String getSelectOrderSQL() {
        return " ORDER BY id";
    }

    @Override
    public List<T> readAll() {
        String sql = getSelectSQL()
                + getSelectGroupSQL()
                + getSelectOrderSQL();
        return jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public T create(T o) {
        Map<String, String> values = this.getValues(o);
        List<String> fieldNames = new ArrayList<>(values.keySet());
        var keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO " + table + " (" + String.join(", ", fieldNames) + ") VALUES (" +
                String.join(", ", addColonBeforeString(fieldNames)) + ")";

        var params = new MapSqlParameterSource(values);

        int rows = namedParameterJdbcTemplate.update(sql, params, keyHolder);
        if (rows > 0) {
            o.setId(keyHolder.getKey().intValue());
            log.info("Запись успешно добавлена, идентификатор=" + keyHolder.getKey().intValue());
        } else {
            log.info("SQL " + sql);
            throw new NotFoundException("Запись не добавлена.");
        }

        return this.read(keyHolder.getKey().intValue());
    }

    @Override
    public T read(Integer id) {
        validateId(id);

        String sql = getSelectSQL() +
                " WHERE " + getTable() + ".id=?" +
                getSelectGroupSQL() +
                getSelectOrderSQL();

        log.info("RUN SQL=" + sql);
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRow, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Запись с идентификатором не найдена.");
        }
    }

    @Override
    public T update(T o) {
        Map<String, String> values = this.getValues(o);
        List<String> fieldNames = new ArrayList<>(values.keySet());
        fieldNames.replaceAll(s -> s + "= :" + s);

        String sql = "UPDATE " + table + " SET " +
                String.join(", ", fieldNames) + " " +
                "WHERE id= :id";
        var params = new MapSqlParameterSource(values);
        int status = namedParameterJdbcTemplate.update(sql, params);

        if (status != 0) {
            log.info("Запись успешно обновлена ");
        } else {
            throw new NotFoundException("Запись с идентификатором не добавлена.");
        }

        return this.read(o.getId());
    }

    @Override
    public void delete(Integer id) {
        validateId(id);
        String sql = "DELETE FROM " + table + " WHERE id=?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows > 0) {
            log.info("Запись удалена.");
        } else {
            throw new NotFoundException("Запись не удалена.");
        }
    }

    @Override
    public void validateId(Integer id) {
        if (id == null) {
            throw new ValidationException("Идентификатор не задан.");
        }
        if (id < 1) {
            throw new NotFoundException("Запись по неверному идентификатору не может быть найдена.");
        }
    }

    protected Integer[] getIDsFromSQLResult(String s) {
        Integer[] result = {};
        if (s == null) {
            return result;
        }

        s = s.substring(1, s.length() - 1);
        if (s.equalsIgnoreCase("NULL")) {
            return result;
        }

        String[] ids = s.split(",");
        result = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            try {
                result[i] = Integer.parseInt(ids[i].trim());
            } catch (NumberFormatException e) {
                log.error("Parsing failed! >" + ids[i] + "< can not be an integer");
            }
        }

        return result;
    }

    static List<String> addColonBeforeString(List<String> targetList) {
        List<String> result = new ArrayList<>(targetList);
        result.replaceAll(s -> ":" + s);
        return result;
    }
}
