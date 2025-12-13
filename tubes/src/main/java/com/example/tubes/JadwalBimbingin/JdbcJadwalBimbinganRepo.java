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

    // Method mapRow milik rekan Anda (JANGAN DIUBAH)
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
            if (name != null)
                j.setNamaMahasiswa(name);
            String npm = rs.getString("npm");
            if (npm != null)
                j.setNpm(npm);
        } catch (Exception e) {
        }

        try {
            String topik = rs.getString("topikTA");
            if (topik != null)
                j.setTopikTA(topik);
        } catch (Exception e) {
        }

        return j;
    }

    @Override
    public List<JadwalBimbingan> findPengajuanByDosen(int idDosen) {
        // Update Query: Tambahkan JOIN ke tabel ruangan dan select namaRuangan
        String sql = """
                    SELECT j.*, u.name, m.npm, r.namaRuangan
                    FROM jadwal_bimbingan j
                    JOIN mahasiswa m ON j.idMhs = m.idMhs
                    JOIN users u ON u.idUser = m.idMhs
                    LEFT JOIN ruangan r ON j.idRuangan = r.idRuangan
                    WHERE j.idDosen = ? AND j.status = 'pending'
                    ORDER BY j.tanggal ASC, j.waktuMulai ASC
                """;

        return jdbc.query(sql, (rs, rowNum) -> {
            // Gunakan mapRow dasar
            JadwalBimbingan j = mapRow(rs);

            // Set data tambahan manual
            try {
                j.setNamaMahasiswa(rs.getString("name"));
            } catch (Exception e) {
            }
            try {
                j.setNpm(rs.getString("npm"));
            } catch (Exception e) {
            }

            // Set Nama Ruangan
            try {
                j.setNamaRuangan(rs.getString("namaRuangan"));
            } catch (Exception e) {
            }

            return j;
        }, idDosen);
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
        String sql = "SELECT * FROM jadwal_bimbingan WHERE idJadwal = ?";
        try {
            JadwalBimbingan j = jdbc.queryForObject(sql, (rs, rowNum) -> mapRow(rs), idJadwal);
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

    @Override
    public List<JadwalBimbingan> findByMahasiswa(int idMhs) {
        // UPDATE QUERY:
        // 1. Tambah LEFT JOIN ke penugasan_ta dan ta untuk ambil topikTA
        // 2. Select t.topikTA
        String sql = """
                    SELECT j.*,
                           u.name as namaDosen,
                           r.namaRuangan,
                           t.topikTA
                    FROM jadwal_bimbingan j
                    JOIN dosen d ON j.idDosen = d.idDosen
                    JOIN users u ON d.idDosen = u.idUser
                    LEFT JOIN ruangan r ON j.idRuangan = r.idRuangan
                    LEFT JOIN penugasan_ta pta ON pta.idMhs = j.idMhs
                    LEFT JOIN ta t ON t.idTA = pta.idTA
                    WHERE j.idMhs = ?
                    ORDER BY j.tanggal DESC, j.waktuMulai DESC
                """;

        return jdbc.query(sql, (rs, rowNum) -> {
            JadwalBimbingan j = mapRow(rs);

            // Set Nama Dosen
            try {
                j.setNamaDosen(rs.getString("namaDosen"));
            } catch (Exception e) {
            }

            // Set Nama Ruangan
            try {
                String ruang = rs.getString("namaRuangan");
                j.setNamaRuangan(ruang != null ? ruang : "-");
            } catch (Exception e) {
                j.setNamaRuangan("-");
            }

            // Set Judul Topik
            try {
                String topik = rs.getString("topikTA");
                j.setTopikTA(topik != null && !topik.isEmpty() ? topik : "Topik belum ditentukan");
            } catch (Exception e) {
                j.setTopikTA("-");
            }

            return j;
        }, idMhs);
    }

    @Override
    public void insertPengajuan(int idMhs, int idDosen, LocalDate tanggal, LocalTime mulai, LocalTime selesai,
            int idRuangan) {
        String sql = """
                INSERT INTO jadwal_bimbingan (idMhs, idDosen, tanggal, waktuMulai, waktuSelesai, idRuangan, status)
                VALUES (?, ?, ?, ?, ?, ?, 'pending')
                """;

        jdbc.update(sql, idMhs, idDosen, tanggal, mulai, selesai, idRuangan);
    }
}
