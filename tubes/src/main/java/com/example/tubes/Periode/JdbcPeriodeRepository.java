package com.example.tubes.Periode;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcPeriodeRepository {

    private final JdbcTemplate jdbc;

    public JdbcPeriodeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<PeriodeTA> findBySemester(int semester) {
        String sql = """
            SELECT idPeriodeTA, TahunAjaran, Semester, TanggalMulaiPeriodeTA, TanggalSelesaiPeriodeTA
            FROM periode_ta
            WHERE Semester = ?
            ORDER BY TanggalMulaiPeriodeTA DESC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            PeriodeTA p = new PeriodeTA();
            p.setIdPeriodeTA(rs.getInt("idPeriodeTA"));
            p.setTahunAjaran(rs.getString("TahunAjaran"));
            p.setSemester(rs.getInt("Semester"));

            Date mulai = rs.getDate("TanggalMulaiPeriodeTA");
            Date selesai = rs.getDate("TanggalSelesaiPeriodeTA");
            p.setTanggalMulaiPeriodeTA(mulai != null ? mulai.toLocalDate() : null);
            p.setTanggalSelesaiPeriodeTA(selesai != null ? selesai.toLocalDate() : null);

            return p;
        }, semester);
    }

    public void insert(String tahunAjaran, int semester, LocalDate mulai, LocalDate selesai) {
        String sql = """
            INSERT INTO periode_ta (TahunAjaran, Semester, TanggalMulaiPeriodeTA, TanggalSelesaiPeriodeTA)
            VALUES (?, ?, ?, ?)
        """;
        jdbc.update(sql, tahunAjaran, semester, Date.valueOf(mulai), Date.valueOf(selesai));
    }

    public void deleteById(int idPeriodeTA) {
        jdbc.update("DELETE FROM periode_ta WHERE idPeriodeTA = ?", idPeriodeTA);
    }
}
