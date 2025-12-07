package com.example.tubes.Dosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcDosenRepo implements DosenRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Menggabungkan data users dan dosen
    private Dosen mapRowToDosen(ResultSet rs, int rowNum) throws SQLException {
        return new Dosen(
                rs.getInt("idDosen"),
                rs.getString("nik"),
                rs.getString("name"),
                rs.getString("email"));
    }

    @Override
    public Optional<Dosen> findByUserId(int idUser) {
        String sql = """
                    SELECT d.idDosen, d.nik, u.name, u.email
                    FROM dosen d
                    JOIN users u ON d.idDosen = u.idUser
                    WHERE u.idUser = ?
                """;

        try {
            Dosen dosen = jdbcTemplate.queryForObject(sql, this::mapRowToDosen, idUser);
            return Optional.ofNullable(dosen);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}