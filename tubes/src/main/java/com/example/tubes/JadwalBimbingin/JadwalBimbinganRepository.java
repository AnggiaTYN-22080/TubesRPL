package com.example.tubes.JadwalBimbingin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class JadwalBimbinganRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mapper: Mengubah hasil query database menjadi Objek Java
    private JadwalBimbingan mapRowToJadwal(ResultSet rs, int rowNum) throws SQLException {
        // PERBAIKAN DI SINI:
        // Kita tidak lagi ambil "lokasi", tapi "nama_ruangan" hasil JOIN
        return new JadwalBimbingan(
                rs.getInt("id"),
                rs.getString("dosen_id"),
                rs.getTimestamp("waktu_mulai").toLocalDateTime(),
                rs.getTimestamp("waktu_selesai").toLocalDateTime(),
                rs.getString("nama_ruangan"), // Masukkan nama ruangan ke field lokasi di Java
                rs.getString("status"));
    }

    public List<JadwalBimbingan> findByDosenId(String dosenId) {
        // PERBAIKAN QUERY:
        // Kita gunakan JOIN agar bisa mendapatkan 'nama_ruangan' berdasarkan
        // 'ruangan_id'
        String sql = """
                    SELECT s.*, r.nama_ruangan
                    FROM slot_jadwal s
                    JOIN ruangan r ON s.ruangan_id = r.id
                    WHERE s.dosen_id = ?
                    ORDER BY s.waktu_mulai ASC
                """;

        return jdbcTemplate.query(sql, this::mapRowToJadwal, dosenId);
    }

    // Ambil data untuk Dropdown Ruangan
    public List<Map<String, Object>> findAllRuangan() {
        return jdbcTemplate.queryForList("SELECT id, nama_ruangan FROM ruangan");
    }

    // Ambil data untuk Dropdown Mahasiswa
    public List<Map<String, Object>> findMahasiswaByDosen(String nikDosen) {
        String sql = "SELECT npm, nama FROM mahasiswa WHERE dosen_pembimbing_id = ?";
        return jdbcTemplate.queryForList(sql, nikDosen);
    }

    // Simpan Jadwal Baru
    public void save(JadwalBimbingan jadwal, String mahasiswaId, int ruanganId) {
        String sql = "INSERT INTO slot_jadwal (dosen_id, waktu_mulai, waktu_selesai, ruangan_id, mahasiswa_id, status) VALUES (?, ?, ?, ?, ?, 'TERJADWAL')";

        jdbcTemplate.update(sql,
                jadwal.getDosenId(),
                jadwal.getWaktuMulai(),
                jadwal.getWaktuSelesai(),
                ruanganId,
                mahasiswaId);
    }
}