package com.example.tubes.Mahasiswa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcMahasiswaRepo implements MahasiswaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Mahasiswa mapRowToMahasiswa(ResultSet rs, int rowNum) throws SQLException {
        return new Mahasiswa(
                rs.getInt("idMhs"),
                rs.getString("npm"),
                rs.getString("name"),
                rs.getString("email"));
    }

    @Override
    public Optional<Mahasiswa> findByUserId(int idUser) {
        String sql = """
                    SELECT m.idMhs, m.npm, u.name, u.email
                    FROM mahasiswa m
                    JOIN users u ON m.idMhs = u.idUser
                    WHERE u.idUser = ?
                """;

        try {
            Mahasiswa mhs = jdbcTemplate.queryForObject(sql, this::mapRowToMahasiswa, idUser);
            return Optional.ofNullable(mhs);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}