package com.example.musicplayer.repository;

import com.example.musicplayer.model.Folder;
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