package ru.yandex.practicum.filmorate.storage.dataBase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("UserDBStorage")
public class UserDBStorage extends AbstractGenericDao<User>  implements UserStorage {

    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "users");
    }

    @Override
    User mapRow(ResultSet resultSet, int i) throws SQLException {
        User result = User.builder()
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        result.setId(resultSet.getInt("id"));
        return result;
    }

    @Override
    protected Map<String, String> getValues(User user) {
        Map<String,String> values = new HashMap<>();
        if (user.getId()!=null) {
            values.put("id",user.getId().toString());
        }
        values.put("name",user.getName());
        values.put("name",user.getName());
        values.put("login",user.getLogin());
        values.put("birthday",user.getBirthday().toString());
        values.put("email",user.getEmail().toString());
        return values;
    }

    @Override
    public void sendOffer(Integer fromUserID, Integer toUserID) {

    }

    @Override
    public void confirm(Integer fromUserID, Integer toUserID) {

    }

    @Override
    public void delete(Integer fromUserID, Integer toUserID) {

    }

    @Override
    public List<Film> getReceivedOffers(Integer toUserID) {
        return null;
    }

    @Override
    public List<Film> getSentOffers(Integer fromUserID) {
        return null;
    }

    @Override
    public List<Film> getFriends(Integer userID) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Integer leftUserId, Integer rightUserId) {
        return null;
    }
}
