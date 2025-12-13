package com.example.tubes.Periode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPeriodeTARepo implements PeriodeTARepository {

    @Autowired
    private JdbcTemplate jdbc;

    private PeriodeTA mapRow(ResultSet rs, int rowNum) throws SQLException {
        PeriodeTA p = new PeriodeTA();

        p.setIdPeriodeTA(rs.getInt("idPeriodeTA"));
        p.setTahunAjaran(rs.getString("TahunAjaran"));
        p.setSemester(rs.getInt("Semester"));

        Date mulai = rs.getDate("TanggalMulaiPeriodeTA");
        Date selesai = rs.getDate("TanggalSelesaiPeriodeTA");

        if (mulai != null)
            p.setTanggalMulaiPeriodeTA(mulai.toLocalDate());
        if (selesai != null)
            p.setTanggalSelesaiPeriodeTA(selesai.toLocalDate());

        return p;
    }

    @Override
    public List<PeriodeTA> findAll() {
        return jdbc.query("""
                    SELECT * FROM periode_ta
                    ORDER BY TahunAjaran DESC, Semester DESC
                """, this::mapRow);
    }

    @Override
    public Optional<PeriodeTA> findById(int id) {
        var list = jdbc.query(
                "SELECT * FROM periode_ta WHERE idPeriodeTA = ?",
                this::mapRow,
                id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public void insert(PeriodeTA p) {
        jdbc.update("""
                    INSERT INTO periode_ta
                    (TahunAjaran, Semester, TanggalMulaiPeriodeTA, TanggalSelesaiPeriodeTA)
                    VALUES (?, ?, ?, ?)
                """,
                p.getTahunAjaran(),
                p.getSemester(),
                java.sql.Date.valueOf(p.getTanggalMulaiPeriodeTA()),
                java.sql.Date.valueOf(p.getTanggalSelesaiPeriodeTA()));
    }

    @Override
    public void update(PeriodeTA p) {
        jdbc.update("""
                    UPDATE periode_ta
                    SET TahunAjaran = ?, Semester = ?,
                        TanggalMulaiPeriodeTA = ?, TanggalSelesaiPeriodeTA = ?
                    WHERE idPeriodeTA = ?
                """,
                p.getTahunAjaran(),
                p.getSemester(),
                java.sql.Date.valueOf(p.getTanggalMulaiPeriodeTA()),
                java.sql.Date.valueOf(p.getTanggalSelesaiPeriodeTA()),
                p.getIdPeriodeTA());
    }

    @Override
    public void delete(int id) {
        jdbc.update("DELETE FROM periode_ta WHERE idPeriodeTA = ?", id);
    }
}
