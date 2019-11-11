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

import java.util.Objects;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    @Autowired
    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User saveUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlInsertToCredential = "INSERT INTO credential (username, password) VALUES (:username, :password)";

        SqlParameterSource parameterSourceCredential = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("password", user.getPassword());
        jdbcTemplate.update(sqlInsertToCredential, parameterSourceCredential, keyHolder, new String[]{"id"});
        int credentialId = (int) Objects.requireNonNull(keyHolder.getKey()).longValue();

        String sqlInsertToUser = "INSERT INTO \"user\" (credential_id, surname, name, role_id) " +
                "VALUES (:credential_id, :surname, :name, :role_id)";

        SqlParameterSource parameterSourceUser = new MapSqlParameterSource()
                .addValue("credential_id", credentialId)
                .addValue("surname", user.getSurname())
                .addValue("name", user.getName())
                .addValue("role_id", 1);
        jdbcTemplate.update(sqlInsertToUser, parameterSourceUser);
        return user;
    }

    public User getUserByUsername(String username) {
        try {
            String sql = "SELECT u.id, c.username, c.password, u.name, u.surname, r.title AS role " +
                    "FROM credential AS c INNER JOIN \"user\" u on c.id = u.credential_id " +
                    "INNER JOIN role r on u.role_id = r.id";
            return jdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource("username", username), userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}