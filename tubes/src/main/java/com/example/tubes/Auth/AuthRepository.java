package com.example.tubes.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<User> findUserByEmail(String email) {
        // 1. Cek Tabel ADMIN
        // PERBAIKAN: Mengubah "id_admin" menjadi "id" sesuai database
        try {
            String sql = "SELECT id, nama, email, password FROM admin WHERE email = ?";

            User u = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    String.valueOf(rs.getInt("id")), // Ubah disini juga
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("password"),
                    "ADMIN"), email);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException ignored) {
        }

        // 2. Cek Tabel DOSEN (Tetap pakai 'nik')
        try {
            String sql = "SELECT nik, nama, email, password FROM dosen WHERE email = ?";
            User u = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    rs.getString("nik"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("password"),
                    "DOSEN"), email);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException ignored) {
        }

        // 3. Cek Tabel MAHASISWA (Tetap pakai 'npm')
        try {
            String sql = "SELECT npm, nama, email, password FROM mahasiswa WHERE email = ?";
            User u = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    rs.getString("npm"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("password"),
                    "MAHASISWA"), email);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException ignored) {
        }

        return Optional.empty();
    }
}