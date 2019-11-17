package com.example.musicplayer.player.repository;

import com.example.musicplayer.player.model.Folder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FolderRowMapper implements RowMapper<Folder> {

    @Override
    public Folder mapRow(ResultSet rs, int rowNum) throws SQLException {
        Folder folder = new Folder();
        folder.setId(rs.getInt("id"));
        folder.setPath(rs.getString("path"));
        return folder;
    }
}