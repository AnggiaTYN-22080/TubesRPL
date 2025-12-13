package com.example.tubes.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcPersyaratanSidangRepo implements PersyaratanSidangRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<PersyaratanSidangItem> findBySemester(int semester) {
        String sql = """
            SELECT
                pt.idMhs,
                pt.idTA,
                um.name  AS namaMahasiswa,
                um.email AS emailMahasiswa,
                m.npm    AS npmMahasiswa,
                pt.StatusPersyaratan,
                p.Semester
            FROM penugasan_ta pt
            JOIN mahasiswa m ON m.idMhs = pt.idMhs
            JOIN users um ON um.idUser = m.idMhs
            JOIN ta t ON t.idTA = pt.idTA
            LEFT JOIN periode_ta p ON p.idPeriodeTA = t.idPeriodeTA
            WHERE p.Semester = ?
            ORDER BY um.name ASC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            String status = rs.getString("StatusPersyaratan");
            boolean terpenuhi = status != null && status.equalsIgnoreCase("terpenuhi");
            String label = terpenuhi ? "TERPENUHI" : "BELUM TERPENUHI";

            PersyaratanSidangItem item = new PersyaratanSidangItem();
            item.setIdMhs(rs.getInt("idMhs"));
            item.setIdTA(rs.getInt("idTA"));
            item.setNama(rs.getString("namaMahasiswa"));
            item.setTerpenuhi(terpenuhi);
            item.setLabelStatus(label);

            // opsional
            item.setEmail(rs.getString("emailMahasiswa"));
            item.setNpm(rs.getString("npmMahasiswa"));

            return item;
        }, semester);
    }
}
