package com.example.tubes.JadwalBimbingin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcJadwalBimbinganRepo implements JadwalBimbinganRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<JadwalBimbingan> findPengajuanByDosen(int idDosen) {
        String sql = """
                SELECT jb.*, u.name AS namaMahasiswa, m.npm
                FROM jadwal_bimbingan jb
                LEFT JOIN mahasiswa m ON jb.idMhs = m.idMhs
                LEFT JOIN users u ON m.idMhs = u.idUser
                WHERE jb.idDosen = ?
                ORDER BY jb.tanggal DESC, jb.waktuMulai DESC
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
                ORDER BY tanggal, waktuMulai
                """;
        return jdbc.query(sql, (rs, rowNum) -> mapRow(rs), idDosen, year, month);
    }

    @Override
    public void insertPengajuan(int idMhs, int idDosen, LocalDate tanggal, LocalTime mulai, LocalTime selesai) {
        String sql = """
                INSERT INTO jadwal_bimbingan (tanggal, waktuMulai, waktuSelesai, status, idMhs, idDosen, idRuangan)
                VALUES (?, ?, ?, 'pending', ?, ?, NULL)
                """;
        jdbc.update(sql, tanggal, mulai, selesai, idMhs, idDosen);
    }

    @Override
    public Optional<JadwalBimbingan> findById(int idJadwal) {
        String sql = "SELECT * FROM jadwal_bimbingan WHERE idJadwal = ?";
        List<JadwalBimbingan> res = jdbc.query(sql, (rs, rowNum) -> mapRow(rs), idJadwal);
        return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
    }

    // =====================
    // ADMIN
    // =====================
    @Override
    public List<JadwalBimbingan> findAll() {
        String sql = """
                SELECT * FROM jadwal_bimbingan
                ORDER BY tanggal DESC, waktuMulai DESC
                """;
        return jdbc.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public void updateRuangan(int idJadwal, Integer idRuangan) {
        String sql = "UPDATE jadwal_bimbingan SET idRuangan = ? WHERE idJadwal = ?";
        jdbc.update(sql, idRuangan, idJadwal);
    }

    private JadwalBimbingan mapRow(ResultSet rs) throws SQLException {
        JadwalBimbingan jb = new JadwalBimbingan();
        jb.setIdJadwal(rs.getInt("idJadwal"));
        jb.setTanggal(rs.getDate("tanggal").toLocalDate());
        jb.setWaktuMulai(rs.getTime("waktuMulai").toLocalTime());
        jb.setWaktuSelesai(rs.getTime("waktuSelesai").toLocalTime());
        jb.setStatus(rs.getString("status"));

        // nullable FK
        int idMhs = rs.getInt("idMhs");
        jb.setIdMhs(rs.wasNull() ? 0 : idMhs);

        int idDosen = rs.getInt("idDosen");
        jb.setIdDosen(rs.wasNull() ? 0 : idDosen);

        int idRuangan = rs.getInt("idRuangan");
        jb.setIdRuangan(rs.wasNull() ? 0 : idRuangan);

        // opsional (kalau query join ada)
        try {
            jb.setNamaMahasiswa(rs.getString("namaMahasiswa"));
            jb.setNpm(rs.getString("npm"));
        } catch (SQLException ignored) {
        }

        return jb;
    }

    @Override
    public List<AdminJadwalRow> findAllAdminRows() {
        String sql = """
                    SELECT
                        jb.idJadwal,
                        jb.status,
                        jb.tanggal,
                        jb.waktuMulai,
                        jb.waktuSelesai,
                        d.nik AS nikDosen,
                        u.name AS namaDosen,
                        jb.idRuangan,
                        r.namaRuangan
                    FROM jadwal_bimbingan jb
                    JOIN dosen d ON d.idDosen = jb.idDosen
                    JOIN users u ON u.idUser = d.idDosen
                    LEFT JOIN ruangan r ON r.idRuangan = jb.idRuangan
                    ORDER BY jb.tanggal DESC, jb.waktuMulai DESC
                """;

        return jdbc.query(sql, (rs, rowNum) -> new AdminJadwalRow(
                rs.getInt("idJadwal"),
                rs.getString("status"),
                rs.getDate("tanggal").toLocalDate(),
                rs.getTime("waktuMulai").toLocalTime(),
                rs.getTime("waktuSelesai").toLocalTime(),
                rs.getString("nikDosen"),
                rs.getString("namaDosen"),
                (Integer) rs.getObject("idRuangan"),
                rs.getString("namaRuangan")));
    }

}
