package com.example.musicplayer.authentication.repository;

import com.example.musicplayer.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    @Autowired
    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT u.id, c.username, c.password, u.name, u.surname, r.title AS role, u.email AS email " +
                "FROM \"user\" AS u INNER JOIN credential AS c on u.credential_id = c.id " +
                "INNER JOIN role r on u.role_id = r.id";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), userRowMapper);
    }

    public User saveUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlInsertToCredential = "INSERT INTO credential (username, password) VALUES (:username, :password)";

        SqlParameterSource parameterSourceCredential = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("password", user.getPassword());
        jdbcTemplate.update(sqlInsertToCredential, parameterSourceCredential, keyHolder, new String[]{"id"});
        int credentialId = (int) Objects.requireNonNull(keyHolder.getKey()).longValue();

        String sqlInsertToUser = "INSERT INTO \"user\" (credential_id, surname, name, role_id, email) " +
                "VALUES (:credential_id, :surname, :name, :role_id, :email)";

        SqlParameterSource parameterSourceUser = new MapSqlParameterSource()
                .addValue("credential_id", credentialId)
                .addValue("surname", user.getSurname())
                .addValue("name", user.getName())
                .addValue("role_id", 1)
                .addValue("email", user.getEmail());
        jdbcTemplate.update(sqlInsertToUser, parameterSourceUser);
        return user;
    }

    public void updateUser(User user, int userId) {
        String sqlUpdateCredential = "UPDATE credential SET username = :username, password = :password WHERE id = :id";
        SqlParameterSource parameterSourceCredential = new MapSqlParameterSource()
                .addValue("password", user.getPassword())
                .addValue("username", user.getUsername())
                .addValue("id", userId);
        jdbcTemplate.update(sqlUpdateCredential, parameterSourceCredential);

        String sqlUpdateUser = "UPDATE \"user\" SET surname = :surname, name = :name, email = :email WHERE id = :id";
        SqlParameterSource parameterSourceUser = new MapSqlParameterSource()
                .addValue("surname", user.getSurname())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("id", userId);
        jdbcTemplate.update(sqlUpdateUser, parameterSourceUser);
    }

    public User getUserByUsername(String username) {
        try {
            String sql = "SELECT u.id, c.username, c.password, u.name, u.surname, r.title AS role, u.email AS email " +
                    "FROM credential AS c INNER JOIN \"user\" u on c.id = u.credential_id " +
                    "INNER JOIN role r on u.role_id = r.id WHERE c.username = :username";
            return jdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource("username", username), userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}