package ru.yandex.practicum.filmorate.storage.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("UserDBStorage")
public class UserDBStorage extends AbstractGenericDao<User> implements UserStorage {

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
        Map<String, String> values = new HashMap<>();
        if (user.getId() != null) {
            values.put("id", user.getId().toString());
        }
        values.put("name", user.getName());
        values.put("login", user.getLogin());
        values.put("birthday", user.getBirthday().toString());
        values.put("email", user.getEmail().toString());
        return values;
    }

    @Override
    public void addFriend(Integer fromUserID, Integer toUserID) {
        validateId(fromUserID);
        validateId(toUserID);
        //Проверяем наличие дружбы
        String sql = "SELECT count(*) FROM friendships as f " +
                "WHERE f.from_user_id=? AND f.to_user_id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.TYPE, fromUserID, toUserID);
        if (count == 0) {
            //Запрос на дружбу/подтверждение не отправлялись
            sql = "INSERT INTO friendships (from_user_id, to_user_id) VALUES  (?, ?)";
            jdbcTemplate.update(sql, fromUserID, toUserID);
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        validateId(userId);
        validateId(friendId);
        String sql = "DELETE FROM friendships WHERE from_user_id = ? AND `to_user_id` = ?";
        Integer status = jdbcTemplate.update(sql, userId, friendId);
        if (status != 0) {
            log.info("Лайк удален");
        } else {
            throw new NotFoundException("Запись о лайке не обнаружена и не удалена.");
        }
    }

    @Override
    public List<User> getFriends(Integer userID) {
        validateId(userID);
        //Друг по ТЗ это тот, кому отправлено предложение вне зависимости от подтверждения
        String sql = "SELECT u.* FROM friendships as f " +
                "INNER JOIN users as u ON f.to_user_id=u.id " +
                "WHERE f.from_user_id=?";
        List<User> result = jdbcTemplate.query(sql, this::mapRow, userID);
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    @Override
    public List<User> getCommonFriends(Integer leftUserId, Integer rightUserId) {
        validateId(leftUserId);
        validateId(rightUserId);
        //Друзъя левого юзера
        String sql = "SELECT u.* FROM friendships as f " +
                "INNER JOIN friendships as f2 ON f.to_user_id=f2.to_user_id AND f2.from_user_id=? " +
                "INNER JOIN users as u ON f.to_user_id=u.id " +
                "WHERE f.from_user_id=?";
        List<User> result = jdbcTemplate.query(sql, this::mapRow, rightUserId, leftUserId);
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }
}
