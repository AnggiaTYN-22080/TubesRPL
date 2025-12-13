package com.example.tubes.Bimbingan;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Repository
public class JdbcBimbinganRepo implements BimbinganRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Optional<Bimbingan> findByJadwal(int idJadwal) {

        String sql = """
            SELECT * FROM bimbingan
            WHERE idJadwal = ?
        """;

        try {
            return Optional.ofNullable(
                jdbc.queryForObject(sql, (rs, rowNum) -> {
                    Bimbingan b = new Bimbingan();
                    b.setIdBimbingan(rs.getInt("idBimbingan"));
                    b.setIdJadwal(rs.getInt("idJadwal"));
                    b.setCatatan(rs.getString("catatan"));
                    b.setFileURL(rs.getString("fileURL"));
                    return b;
                }, idJadwal)
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void saveOrUpdate(int idJadwal, String catatan) {

        String checkSql = "SELECT COUNT(*) FROM bimbingan WHERE idJadwal = ?";
        Integer count = jdbc.queryForObject(checkSql, Integer.class, idJadwal);

        if (count != null && count > 0) {
            jdbc.update("""
                UPDATE bimbingan
                SET catatan = ?
                WHERE idJadwal = ?
            """, catatan, idJadwal);
        } else {
            jdbc.update("""
                INSERT INTO bimbingan (idJadwal, catatan)
                VALUES (?, ?)
            """, idJadwal, catatan);
        }
    }
}
