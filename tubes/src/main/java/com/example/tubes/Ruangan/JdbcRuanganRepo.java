package com.example.tubes.Ruangan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcRuanganRepo implements RuanganRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<Ruangan> findAll() {
        String sql = "SELECT * FROM ruangan ORDER BY idRuangan";
        return jdbc.query(sql, (rs, rowNum) -> {
            Ruangan r = new Ruangan();
            r.setIdRuangan(rs.getInt("idRuangan"));
            r.setNamaRuangan(rs.getString("namaRuangan"));
            r.setStatusRuangan(rs.getString("statusRuangan"));
            return r;
        });
    }

    @Override
    public Optional<Ruangan> findById(int idRuangan) {
        String sql = "SELECT * FROM ruangan WHERE idRuangan = ?";
        List<Ruangan> res = jdbc.query(sql, (rs, rowNum) -> {
            Ruangan r = new Ruangan();
            r.setIdRuangan(rs.getInt("idRuangan"));
            r.setNamaRuangan(rs.getString("namaRuangan"));
            r.setStatusRuangan(rs.getString("statusRuangan"));
            return r;
        }, idRuangan);

        return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
    }

    @Override
    public int insert(String namaRuangan, String statusRuangan) {
        String sql = "INSERT INTO ruangan (namaRuangan, statusRuangan) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"idRuangan"});
            ps.setString(1, namaRuangan);
            ps.setString(2, statusRuangan);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key == null ? 0 : key.intValue();
    }

    @Override
    public boolean update(int idRuangan, String namaRuangan, String statusRuangan) {
        String sql = "UPDATE ruangan SET namaRuangan = ?, statusRuangan = ? WHERE idRuangan = ?";
        int rows = jdbc.update(sql, namaRuangan, statusRuangan, idRuangan);
        return rows > 0;
    }

    @Override
    public boolean delete(int idRuangan) {
        String sql = "DELETE FROM ruangan WHERE idRuangan = ?";
        int rows = jdbc.update(sql, idRuangan);
        return rows > 0;
    }
}
