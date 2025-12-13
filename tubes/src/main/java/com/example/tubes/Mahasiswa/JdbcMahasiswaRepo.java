package com.example.tubes.Mahasiswa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcMahasiswaRepo implements MahasiswaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Mahasiswa mapRowToMahasiswa(ResultSet rs, int rowNum) throws SQLException {
        return new Mahasiswa(
                rs.getInt("idUser"),
                rs.getString("npm"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"));
    }

    @Override
    public Optional<Mahasiswa> findByUserId(int idUser) {
        String sql = """
                    SELECT u.idUser, u.name, u.email, u.password, u.role, m.npm
                    FROM users u
                    JOIN mahasiswa m ON u.idUser = m.idMhs
                    WHERE u.idUser = ?
                """;
        try {
            Mahasiswa mhs = jdbcTemplate.queryForObject(sql, this::mapRowToMahasiswa, idUser);
            return Optional.ofNullable(mhs);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Object> findTopikTA(int idMhs) {
        String sql = """
                    SELECT t.TopikTA
                    FROM penugasan_ta p
                    JOIN ta t ON p.idTA = t.idTA
                    WHERE p.idMhs = ?
                """;
        try {
            return jdbcTemplate.queryForMap(sql, idMhs);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Object> findNextBimbingan(int idMhs) {
        String sql = """
                    SELECT tanggal, waktuMulai
                    FROM jadwal_bimbingan
                    WHERE idMhs = ? AND tanggal >= CURRENT_DATE
                    ORDER BY tanggal ASC, waktuMulai ASC
                    LIMIT 1
                """;
        try {
            return jdbcTemplate.queryForMap(sql, idMhs);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyMap();
        }
    }

    @Override
    public Optional<String> findNamaDosenPembimbing(int idMhs) {
        String sql = """
                SELECT u.name AS namaDosen
                FROM penugasan_ta p
                JOIN ta t ON p.idTA = t.idTA
                JOIN dosen d ON t.idDosen = d.idDosen
                JOIN users u ON d.idDosen = u.idUser
                WHERE p.idMhs = ?
                LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, String.class, idMhs));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> findIdDosenPembimbing(int idMhs) {
        String sql = """
                SELECT t.idDosen
                FROM penugasan_ta p
                JOIN ta t ON p.idTA = t.idTA
                WHERE p.idMhs = ?
                LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, idMhs));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}