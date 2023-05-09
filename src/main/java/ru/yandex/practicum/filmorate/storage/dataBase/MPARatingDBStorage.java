package ru.yandex.practicum.filmorate.storage.dataBase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component("MPARatingDBStorage")
public class MPARatingDBStorage extends AbstractGenericDao<MPARating> implements MPARatingStorage {

    //Кэш для ускорения обработки запросов
    HashMap<Integer,MPARating> cache;

    public MPARatingDBStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "mpa_ratings");
        cache = new HashMap<>();
    }

    @Override
    protected MPARating mapRow(ResultSet resultSet, int i) throws SQLException {
        MPARating result = new MPARating(resultSet.getString("name"),
                                        resultSet.getString("description"));
        result.setId(resultSet.getInt("id"));
        return result;
    }

    @Override
    protected Map<String,String> getValues(MPARating mpaRating) {
        Map<String,String> values = new HashMap<>();
        if (mpaRating.getId()!=null) {
            values.put("id",mpaRating.getId().toString());
        }
        values.put("name",mpaRating.getName());
        values.put("description",mpaRating.getDescription());
        return values;
    }

    @Override
    public MPARating read(Integer id) {
        validateId(id);
        if (!cache.containsKey(id)) {
            cache.put(id,super.read(id));
        }
        return cache.get(id);
    }

    @Override
    public MPARating update(MPARating o) {
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
}
