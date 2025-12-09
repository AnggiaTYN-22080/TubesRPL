package com.example.tubes.JadwalBimbingin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcJadwalBimbinganRepo implements JadwalBimbinganRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private JadwalBimbingan mapRow(ResultSet rs) throws SQLException {
        JadwalBimbingan j = new JadwalBimbingan();

        j.setIdJadwal(rs.getInt("idJadwal"));
        j.setTanggal(rs.getDate("tanggal").toLocalDate());
        j.setWaktuMulai(rs.getTime("waktuMulai").toLocalTime());
        j.setWaktuSelesai(rs.getTime("waktuSelesai").toLocalTime());
        j.setStatus(rs.getString("status"));

        j.setIdMhs(rs.getInt("idMhs"));
        j.setIdDosen(rs.getInt("idDosen"));
        j.setIdRuangan(rs.getInt("idRuangan"));

        // Optional: jika query memiliki kolom mahasiswa
        try {
            j.setNamaMahasiswa(rs.getString("name"));
            j.setNpm(rs.getString("npm"));
        } catch (Exception ignored) {}

        return j;
    }

    @Override
    public List<JadwalBimbingan> findPengajuanByDosen(int idDosen) {

        String sql = """
                SELECT j.*, u.name, m.npm
                FROM jadwal_bimbingan j
                JOIN mahasiswa m ON j.idMhs = m.idMhs
                JOIN users u ON u.idUser = m.idMhs
                WHERE j.idDosen = ? AND j.status = 'pending'
                ORDER BY j.tanggal ASC
        """;

        return jdbc.query(sql, (rs, rowNum) -> mapRow(rs), idDosen);
    }

    @Override
    public void updateStatus(int idJadwal, String newStatus) {
        String sql = "UPDATE jadwal_bimbingan SET status = ? WHERE idJadwal = ?";
        jdbc.update(sql, newStatus, idJadwal);
    }

    @Override
    public List<JadwalBimbingan> findByMonth(int idDosen, int year, int month) {

        String sql = """
            SELECT * FROM jadwal_bimbingan
            WHERE idDosen = ? 
              AND EXTRACT(YEAR FROM tanggal) = ?
              AND EXTRACT(MONTH FROM tanggal) = ?
            ORDER BY tanggal
        """;

        return jdbc.query(sql, (rs, rowNum) -> mapRow(rs), idDosen, year, month);
    }
}
