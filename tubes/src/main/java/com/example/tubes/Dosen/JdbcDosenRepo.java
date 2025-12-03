package com.example.tubes.Dosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcDosenRepo implements DosenRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Dosen mapRowToDosen(ResultSet rs, int rowNum) throws SQLException {
        return new Dosen(
                rs.getString("nik"),
                rs.getString("nama"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("kuota_bimbingan"));
    }

    @Override
    public List<Dosen> findAll() {
        return jdbcTemplate.query("SELECT * FROM dosen", this::mapRowToDosen);
    }

    @Override
    public Optional<Dosen> findByNik(String nik) {
        String sql = "SELECT * FROM dosen WHERE nik = ?";
        List<Dosen> results = jdbcTemplate.query(sql, this::mapRowToDosen, nik);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public void save(Dosen dosen) {
        String sql = "INSERT INTO dosen (nik, nama, email, password, kuota_bimbingan) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dosen.getNik(), dosen.getNama(), dosen.getEmail(), dosen.getPassword(),
                dosen.getKuotaBimbingan());
    }

    @Override
    public void update(Dosen dosen) {
        String sql = "UPDATE dosen SET nama=?, email=?, password=?, kuota_bimbingan=? WHERE nik=?";
        jdbcTemplate.update(sql, dosen.getNama(), dosen.getEmail(), dosen.getPassword(), dosen.getKuotaBimbingan(),
                dosen.getNik());
    }

    @Override
    public void delete(String nik) {
        jdbcTemplate.update("DELETE FROM dosen WHERE nik=?", nik);
    }
}