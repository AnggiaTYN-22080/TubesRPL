package com.example.tubes.Mahasiswa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.tubes.JadwalBimbingin.JadwalBimbingan;

public class JdbcMahasiswaRepo implements MahasiswaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mapper untuk Jadwal (Copy dari JadwalRepository biar cepat)
    private JadwalBimbingan mapRowToJadwal(ResultSet rs, int rowNum) throws SQLException {
        return new JadwalBimbingan(
                rs.getInt("id"),
                rs.getString("dosen_id"),
                rs.getTimestamp("waktu_mulai").toLocalDateTime(),
                rs.getTimestamp("waktu_selesai").toLocalDateTime(),
                rs.getString("nama_ruangan"),
                rs.getString("status"));
    }

    // Cari jadwal bimbingan milik mahasiswa tertentu
    public List<JadwalBimbingan> findJadwalByMahasiswaId(String npm) {
        String sql = """
                    SELECT s.*, r.nama_ruangan
                    FROM slot_jadwal s
                    JOIN ruangan r ON s.ruangan_id = r.id
                    WHERE s.mahasiswa_id = ?
                    ORDER BY s.waktu_mulai ASC
                """;
        return jdbcTemplate.query(sql, this::mapRowToJadwal, npm);
    }
}
