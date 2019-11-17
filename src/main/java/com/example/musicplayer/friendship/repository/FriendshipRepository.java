package com.example.musicplayer.friendship.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FriendshipRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final FriendshipRowMapper friendshipRowMapper = new FriendshipRowMapper();

    @Autowired
    public FriendshipRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Integer> getFriendsByUserId(int userId) {
        String sql = "SELECT f.second_friend FROM friendship AS f INNER JOIN \"user\" AS u ON f.first_friend = u.id WHERE first_friend = :first_friend";
        SqlParameterSource parameterSource = new MapSqlParameterSource("first_friend", userId);
        return jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
    }

    public void createFriendship(int currentUserId, int secondUserId) {
        String firstSql = "INSERT INTO friendship (first_friend, second_friend) VALUES(:first_friend, :second_friend)";
        SqlParameterSource firstParameterSource = new MapSqlParameterSource()
                .addValue("first_friend", currentUserId)
                .addValue("second_friend", secondUserId);
        jdbcTemplate.update(firstSql, firstParameterSource);

        String secondSql = "INSERT INTO friendship (first_friend, second_friend) VALUES(:first_friend, :second_friend)";
        SqlParameterSource secondParameterSource = new MapSqlParameterSource()
                .addValue("first_friend", secondUserId)
                .addValue("second_friend", currentUserId);
        jdbcTemplate.update(secondSql, secondParameterSource);
    }
}