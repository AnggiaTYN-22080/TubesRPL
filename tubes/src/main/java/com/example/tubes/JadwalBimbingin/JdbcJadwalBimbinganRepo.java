package com.example.tubes.JadwalBimbingin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.tubes.Ruangan.Ruangan;
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

        try {
            j.setNamaRuangan(rs.getString("namaRuangan"));
        } catch (Exception e) {
            j.setNamaRuangan("-");
        }
        // Optional: jika query memiliki kolom mahasiswa
        try {
            String name = rs.getString("name");
            if (name != null) {
                j.setNamaMahasiswa(name);
            }
            String npm = rs.getString("npm");
            if (npm != null) {
                j.setNpm(npm);
            }
        } catch (Exception e) {
            // Field tidak ada di query
        }
        
        // Optional: jika query memiliki topikTA
        try {
            String topik = rs.getString("topikTA");
            if (topik != null) {
                j.setTopikTA(topik);
            }
        } catch (Exception e) {
            // Field tidak ada di query
        }

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
                    SELECT j.*, 
                        u.name, 
                        m.npm,
                        COALESCE(t.topikTA, '-') AS topikTA,
                        r.namaRuangan
                    FROM jadwal_bimbingan j
                    JOIN mahasiswa m ON j.idMhs = m.idMhs
                    JOIN users u ON u.idUser = m.idMhs
                    LEFT JOIN ruangan r ON r.idRuangan = j.idRuangan
                    LEFT JOIN penugasan_ta pta ON pta.idMhs = m.idMhs
                    LEFT JOIN ta t ON t.idTA = pta.idTA AND t.idDosen = j.idDosen
                    WHERE j.idDosen = ?
                    AND EXTRACT(YEAR FROM j.tanggal) = ?
                    AND EXTRACT(MONTH FROM j.tanggal) = ?
                    AND j.status = 'approved'
                    ORDER BY j.tanggal, j.waktuMulai
                """;

        return jdbc.query(sql, (rs, rowNum) -> mapRow(rs), idDosen, year, month);
    }

    @Override
    public void insertPengajuan(int idMhs, int idDosen, LocalDate tanggal, LocalTime mulai, LocalTime selesai) {
        String sql = """
                INSERT INTO jadwal_bimbingan (idMhs, idDosen, tanggal, waktuMulai, waktuSelesai, status)
                VALUES (?, ?, ?, ?, ?, 'pending')
                """;

        jdbc.update(sql, idMhs, idDosen, tanggal, mulai, selesai);
    }

    @Override
    public Optional<JadwalBimbingan> findById(int idJadwal) {

        String sql = """
                SELECT * FROM jadwal_bimbingan
                WHERE idJadwal = ?
                """;

        try {
            JadwalBimbingan j = jdbc.queryForObject(
                    sql,
                    (rs, rowNum) -> mapRow(rs),
                    idJadwal
            );
            return Optional.ofNullable(j);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Ruangan> findAvailableRuangan(LocalDate tanggal, LocalTime mulai, LocalTime selesai) {

        String sql = """
            SELECT r.idRuangan, r.namaRuangan
            FROM ruangan r
            WHERE NOT EXISTS (
                SELECT 1
                FROM jadwal_bimbingan j
                WHERE j.idRuangan = r.idRuangan
                AND j.tanggal = ?
                AND j.status IN ('pending','approved')
                AND (j.waktuMulai < ? AND j.waktuSelesai > ?)
            )
            ORDER BY r.namaRuangan
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            Ruangan r = new Ruangan();
            r.setIdRuangan(rs.getInt("idRuangan"));
            r.setNamaRuangan(rs.getString("namaRuangan"));
            return r;
        }, tanggal, selesai, mulai);
    }

    @Override
    public void insertPengajuanDosen(int idMhs, int idDosen, int idRuangan,
                                    LocalDate tanggal, LocalTime mulai, LocalTime selesai, String status) {

        String sql = """
            INSERT INTO jadwal_bimbingan (idMhs, idDosen, idRuangan, tanggal, waktuMulai, waktuSelesai, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        jdbc.update(sql, idMhs, idDosen, idRuangan, tanggal, mulai, selesai, status);
    }
}
