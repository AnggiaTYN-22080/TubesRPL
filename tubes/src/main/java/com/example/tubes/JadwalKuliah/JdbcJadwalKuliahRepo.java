package com.example.tubes.JadwalKuliah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcJadwalKuliahRepo implements JadwalKuliahRepository {

    @Autowired
    private JdbcTemplate jdbc;

    // Mapping dari Database ke Java Object
    private JadwalKuliah mapRow(ResultSet rs, int rowNum) throws SQLException {
        JadwalKuliah jk = new JadwalKuliah();
        jk.setIdJadwalKuliah(rs.getInt("idJadwalKuliah"));
        jk.setHari(rs.getString("hari"));
        jk.setJamMulai(rs.getTime("jamMulai").toLocalTime());
        jk.setJamSelesai(rs.getTime("jamSelesai").toLocalTime());
        jk.setKelas(rs.getString("kelas"));
        jk.setKeterangan(rs.getString("keterangan"));
        return jk;
    }

    @Override
    public List<JadwalKuliah> findByMhs(int idMhs) {
        // Query JOIN untuk mengambil jadwal milik mahasiswa tertentu
        String sql = """
                    SELECT jk.* FROM jadwal_kuliah jk
                    JOIN jadwalKuliahMahasiswa jkm ON jk.idJadwalKuliah = jkm.idJadwalKuliah
                    WHERE jkm.idMhs = ?
                    ORDER BY
                        CASE
                            WHEN jk.hari = 'Senin' THEN 1
                            WHEN jk.hari = 'Selasa' THEN 2
                            WHEN jk.hari = 'Rabu' THEN 3
                            WHEN jk.hari = 'Kamis' THEN 4
                            WHEN jk.hari = 'Jumat' THEN 5
                            WHEN jk.hari = 'Sabtu' THEN 6
                            ELSE 7
                        END,
                        jk.jamMulai
                """;
        return jdbc.query(sql, this::mapRow, idMhs);
    }

    @Override
    public void saveCustom(int idMhs, JadwalKuliah jk) {
        // LANGKAH 1: Insert ke tabel Master (jadwal_kuliah) dan ambil ID barunya
        String sqlMaster = """
                    INSERT INTO jadwal_kuliah (hari, jamMulai, jamSelesai, kelas, keterangan)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING idJadwalKuliah
                """;

        Integer newId = jdbc.queryForObject(sqlMaster, Integer.class,
                jk.getHari(),
                jk.getJamMulai(),
                jk.getJamSelesai(),
                jk.getKelas(),
                jk.getKeterangan());

        // LANGKAH 2: Insert ke tabel Relasi (jadwalKuliahMahasiswa)
        if (newId != null) {
            String sqlRelasi = "INSERT INTO jadwalKuliahMahasiswa (idMhs, idJadwalKuliah) VALUES (?, ?)";
            jdbc.update(sqlRelasi, idMhs, newId);
        }
    }
}