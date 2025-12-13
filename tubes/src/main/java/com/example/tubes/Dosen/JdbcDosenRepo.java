package com.example.tubes.Dosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.tubes.JadwalBimbingin.JadwalBimbingan;
import com.example.tubes.Mahasiswa.MahasiswaBimbingan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcDosenRepo implements DosenRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Dosen mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Dosen(
                rs.getInt("idDosen"),
                rs.getString("nik"),
                rs.getString("name"),
                rs.getString("email"));
    }

    @Override
    public Optional<Dosen> findByUserId(int idUser) {
        String sql = """
                SELECT d.idDosen, d.nik, u.name, u.email
                FROM dosen d
                JOIN users u ON d.idDosen = u.idUser
                WHERE u.idUser = ?
                """;

        try {
            Dosen dosen = jdbcTemplate.queryForObject(sql, this::mapRow, idUser);
            return Optional.ofNullable(dosen);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public int countMahasiswaBimbingan(int idDosen) {
        String sql = """
                    SELECT COUNT(*)
                    FROM penugasan_ta p
                    JOIN ta t ON t.idTA = p.idTA
                    WHERE t.idDosen = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, idDosen);
    }

    public int countPengajuanBimbingan(int idDosen) {
        String sql = """
                    SELECT COUNT(*)
                    FROM jadwal_bimbingan
                    WHERE idDosen = ? AND status = 'pending'
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, idDosen);
    }

    @Override
    public List<MahasiswaBimbingan> findMahasiswaBimbingan(int idDosen) {

        String sql = """
            SELECT 
                m.idMhs,
                u.name AS nama,
                m.npm,
                u.email
            FROM penugasan_ta p
            JOIN ta t ON t.idTA = p.idTA
            JOIN mahasiswa m ON m.idMhs = p.idMhs
            JOIN users u ON u.idUser = m.idMhs
            WHERE t.idDosen = ?
            ORDER BY u.name
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MahasiswaBimbingan(
                rs.getInt("idMhs"),
                rs.getString("nama"),
                rs.getString("npm"),
                rs.getString("email")
            ),
            idDosen
        );
    }

    @Override
    public List<JadwalBimbingan> findRiwayatBimbinganMahasiswa(int idMhs) {
        String sql = """
            SELECT *
            FROM jadwal_bimbingan
            WHERE idMhs = ? AND status = 'approved'
            ORDER BY tanggal DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            JadwalBimbingan j = new JadwalBimbingan();
            j.setIdJadwal(rs.getInt("idJadwal"));
            j.setTanggal(rs.getDate("tanggal").toLocalDate());
            j.setWaktuMulai(rs.getTime("waktuMulai").toLocalTime());
            j.setWaktuSelesai(rs.getTime("waktuSelesai").toLocalTime());
            j.setStatus(rs.getString("status"));
            return j;
        }, idMhs);
    }

    @Override
    public List<Map<String, Object>> findJadwalMengajar(int idDosen) {

        String sql = """
            SELECT 
                jk.hari AS hari,
                to_char(jk.jamMulai, 'HH24:MI') AS jamMulai,
                to_char(jk.jamSelesai, 'HH24:MI') AS jamSelesai,
                jk.keterangan AS mataKuliah,
                jk.kelas AS kelas
            FROM jadwalKuliahDosen jkd
            JOIN jadwal_kuliah jk ON jk.idJadwalKuliah = jkd.idJadwalKuliah
            WHERE jkd.idDosen = ?
            ORDER BY
                CASE jk.hari
                    WHEN 'Senin' THEN 1
                    WHEN 'Selasa' THEN 2
                    WHEN 'Rabu' THEN 3
                    WHEN 'Kamis' THEN 4
                    WHEN 'Jumat' THEN 5
                    WHEN 'Sabtu' THEN 6
                    WHEN 'Minggu' THEN 7
                    ELSE 8
                END,
                jk.jamMulai
        """;

        return jdbcTemplate.queryForList(sql, idDosen);
    }
}
