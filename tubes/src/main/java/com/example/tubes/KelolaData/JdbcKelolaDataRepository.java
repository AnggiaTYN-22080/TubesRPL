package com.example.tubes.KelolaData;

import com.example.tubes.KelolaData.dto.PeriodeOption;
import com.example.tubes.KelolaData.dto.PersonRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcKelolaDataRepository implements KelolaDataRepository {

    private final JdbcTemplate jdbc;

    public JdbcKelolaDataRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<PersonRow> findAllMahasiswa() {
        String sql = """
            SELECT m.idMhs AS id,
                   u.name AS nama,
                   m.npm  AS nim
            FROM mahasiswa m
            JOIN users u ON u.idUser = m.idMhs
            ORDER BY u.name
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            PersonRow v = new PersonRow();
            v.setId(rs.getInt("id"));
            v.setNama(rs.getString("nama"));
            v.setNim(rs.getString("nim"));
            return v;
        });
    }

    @Override
    public List<PersonRow> findAllDosen() {
        String sql = """
            SELECT d.idDosen AS id,
                   u.name  AS nama,
                   d.nik   AS nim
            FROM dosen d
            JOIN users u ON u.idUser = d.idDosen
            ORDER BY u.name
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            PersonRow v = new PersonRow();
            v.setId(rs.getInt("id"));
            v.setNama(rs.getString("nama"));
            v.setNim(rs.getString("nim"));
            return v;
        });
    }

    @Override
    public List<PeriodeOption> findAllPeriode() {
        String sql = """
            SELECT idPeriodeTA AS id,
                   CONCAT('TA ', Semester, ' ', TahunAjaran) AS namaPeriode
            FROM periode_ta
            ORDER BY TanggalMulaiPeriodeTA DESC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            PeriodeOption p = new PeriodeOption();
            p.setId(rs.getInt("id"));
            p.setNamaPeriode(rs.getString("namaPeriode"));
            return p;
        });
    }

    @Override
    public List<PeriodeOption> findPeriodeBySemester(int semester) {
        String sql = """
            SELECT idPeriodeTA AS id,
                   CONCAT('TA ', Semester, ' ', TahunAjaran) AS namaPeriode
            FROM periode_ta
            WHERE Semester = ?
            ORDER BY TanggalMulaiPeriodeTA DESC
        """;

        return jdbc.query(sql, ps -> ps.setInt(1, semester), (rs, rowNum) -> {
            PeriodeOption p = new PeriodeOption();
            p.setId(rs.getInt("id"));
            p.setNamaPeriode(rs.getString("namaPeriode"));
            return p;
        });
    }
}
