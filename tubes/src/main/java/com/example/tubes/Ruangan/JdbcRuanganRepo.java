package com.example.tubes.Ruangan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcRuanganRepo implements RuanganRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private Ruangan mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ruangan r = new Ruangan();
        r.setIdRuangan(rs.getInt("idRuangan"));
        r.setNamaRuangan(rs.getString("namaRuangan"));
        r.setStatusRuangan(rs.getString("statusRuangan"));
        return r;
    }

    @Override
    public List<Ruangan> findAvailableRooms(LocalDate tanggal, LocalTime jamMulai, LocalTime jamSelesai) {
        String sql = """
                    SELECT r.* FROM ruangan r
                    WHERE r.statusRuangan = 'tersedia'
                    AND r.idRuangan NOT IN (
                        SELECT j.idRuangan
                        FROM jadwal_bimbingan j
                        WHERE j.tanggal = ?
                          AND j.status != 'rejected' -- Abaikan jadwal yang ditolak (karena ruangannya jadi free)
                          AND j.idRuangan IS NOT NULL
                          AND (
                              -- Logika Cek Bentrok Waktu:
                              (j.waktuMulai < ? AND j.waktuSelesai > ?)
                          )
                    )
                """;

        // Parameter urutan: tanggal, jamSelesai (user), jamMulai (user)
        return jdbc.query(sql, this::mapRow, tanggal, jamSelesai, jamMulai);
    }
}