package com.example.tubes.KelolaData;

import com.example.tubes.KelolaData.dto.TopikRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class JdbcTopikTARepository {

    private final JdbcTemplate jdbc;

    public JdbcTopikTARepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<TopikRow> findRowsByPeriode(int periodeId) {
        // tampilkan mahasiswa yang sudah punya penugasan di periode tsb
        String sql = """
            SELECT 
                m.idMhs AS id,
                uM.nama AS nama,
                m.npm AS npm,
                t.codeTA AS kodeTa,
                t.topikTA AS topik,
                t.idDosen AS dosenId,
                uD.nama AS dosenNama
            FROM penugasan_ta pt
            JOIN mahasiswa m ON m.idMhs = pt.idMhs
            JOIN users uM ON uM.idUser = m.idMhs
            JOIN ta t ON t.idTA = pt.idTA
            LEFT JOIN users uD ON uD.idUser = t.idDosen
            WHERE t.idPeriodeTA = ?
            ORDER BY uM.nama
        """;

        return jdbc.query(sql, (rs, i) -> {
            TopikRow r = new TopikRow();
            r.setId(rs.getInt("id"));
            r.setNama(rs.getString("nama"));
            r.setNpm(rs.getString("npm"));
            r.setKodeTa(rs.getString("kodeTa"));
            r.setTopik(rs.getString("topik"));
            int did = rs.getInt("dosenId");
            r.setDosenId(rs.wasNull() ? null : did);
            r.setDosenNama(rs.getString("dosenNama"));
            return r;
        }, periodeId);
    }

    public Integer findTaId(int mahasiswaId, int periodeId) {
        String sql = """
            SELECT t.idTA
            FROM penugasan_ta pt
            JOIN ta t ON t.idTA = pt.idTA
            WHERE pt.idMhs = ? AND t.idPeriodeTA = ?
            LIMIT 1
        """;
        List<Integer> ids = jdbc.query(sql, (rs, i) -> rs.getInt(1), mahasiswaId, periodeId);
        return ids.isEmpty() ? null : ids.get(0);
    }

    public int insertTa(String codeTA, String topikTA, int dosenId, int periodeId) {
        jdbc.update("""
            INSERT INTO ta (codeTA, topikTA, idDosen, idPeriodeTA)
            VALUES (?, ?, ?, ?)
        """, codeTA, topikTA, dosenId, periodeId);

        // ambil id terakhir (MySQL). Kalau DB kamu bukan MySQL, bilang ya nanti aku sesuaikan.
        Integer id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        return id == null ? 0 : id;
    }

    public void insertPenugasan(int mahasiswaId, int taId) {
        jdbc.update("""
            INSERT INTO penugasan_ta (idMhs, idTA, StatusPersyaratan)
            VALUES (?, ?, 'belum terpenuhi')
        """, mahasiswaId, taId);
    }

    public void updateTa(int taId, String codeTA, String topikTA) {
        jdbc.update("""
            UPDATE ta
            SET codeTA = ?, topikTA = ?
            WHERE idTA = ?
        """, codeTA, topikTA, taId);
    }

    @Transactional
    public void resetMahasiswaPeriode(int mahasiswaId, int periodeId) {
        Integer taId = findTaId(mahasiswaId, periodeId);
        if (taId == null) return;

        jdbc.update("DELETE FROM penugasan_ta WHERE idMhs = ? AND idTA = ?", mahasiswaId, taId);
        jdbc.update("DELETE FROM ta WHERE idTA = ?", taId);
    }
}
