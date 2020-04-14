package com.example.musicplayer.friendship.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class FriendshipRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Integer> getFriendsByUserId(int userId) {
        String sql = ""
                + "SELECT f.second_friend "
                + "FROM friendship AS f INNER JOIN \"user\" AS u ON f.first_friend = u.id "
                + "WHERE first_friend = :userId";
        List<Integer> allFriendsByUser = jdbcTemplate.queryForList(sql, new MapSqlParameterSource("userId", userId), Integer.class);
        if (allFriendsByUser.size() == 0) {
            String sql2 = ""
                    + "SELECT f.second_friend "
                    + "FROM friendship AS f INNER JOIN \"user\" AS u ON f.first_friend = u.id "
                    + "WHERE second_friend = :userId";
            return jdbcTemplate.queryForList(sql2, new MapSqlParameterSource("userId", userId), Integer.class);
        }
        return allFriendsByUser;
    }

    public Integer isFriendHasByUserId(int friendId, int userId) {
        Integer integer;
        try {
            String sql = "SELECT COUNT(*) FROM friendship WHERE first_friend = :userId AND second_friend = :friendId";
            integer = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource()
                    .addValue("friendId", friendId)
                    .addValue("userId", userId), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            integer = 0;
        }
        if (Objects.equals(integer, 0)) {
            String sql2 = "SELECT COUNT(*) FROM friendship WHERE first_friend = :userId AND second_friend = :friendId";
            return jdbcTemplate.queryForObject(sql2, new MapSqlParameterSource()
                    .addValue("friendId", userId)
                    .addValue("userId", friendId), Integer.class);
        }
        return integer;
    }

    public void createFriendship(int currentUserId, int secondUserId) {
        String sql = "INSERT INTO friendship (first_friend, second_friend) VALUES(:first_friend, :second_friend)";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("first_friend", currentUserId)
                .addValue("second_friend", secondUserId));
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("first_friend", secondUserId)
                .addValue("second_friend", currentUserId));
    }

    public void deleteFriendship(int currentUserId, int secondUserId) {
        String sql = "DELETE FROM friendship WHERE first_friend = :currentUserId AND second_friend = :secondUserId";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("currentUserId", currentUserId)
                .addValue("secondUserId", secondUserId));
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("currentUserId", secondUserId)
                .addValue("secondUserId", currentUserId));
    }
}