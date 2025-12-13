package com.example.tubes.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class JdbcAdminBimbinganRepo implements AdminBimbinganRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private final Locale ID = new Locale("id", "ID");
    private final DateTimeFormatter fmtTanggal = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", ID);
    private final DateTimeFormatter fmtJam = DateTimeFormatter.ofPattern("HH.mm");

    @Override
    public List<AdminRiwayatBimbinganRow> findRiwayatAll() {

        // Riwayat = jadwal yang sudah ada catatan bimbingan (bimbingan.idJadwal ada)
        // dan/atau status 'done'. Kalau kamu mau strict hanya done, tinggal aktifkan
        // WHERE status='done'.
        String sql = """
                    SELECT
                        jb.idJadwal,
                        um.name AS namaMahasiswa,
                        m.npm AS npm,
                        ud.name AS namaDosen,
                        d.nik AS nikDosen,
                        jb.tanggal,
                        jb.waktuMulai,
                        jb.waktuSelesai,
                        jb.status,
                        r.namaRuangan
                    FROM jadwal_bimbingan jb
                    JOIN mahasiswa m ON m.idMhs = jb.idMhs
                    JOIN users um ON um.idUser = m.idMhs
                    JOIN dosen d ON d.idDosen = jb.idDosen
                    JOIN users ud ON ud.idUser = d.idDosen
                    LEFT JOIN ruangan r ON r.idRuangan = jb.idRuangan
                    LEFT JOIN bimbingan b ON b.idJadwal = jb.idJadwal
                    WHERE b.idJadwal IS NOT NULL
                    ORDER BY jb.tanggal DESC, jb.waktuMulai DESC
                """;

        return jdbc.query(sql, (rs, rowNum) -> {
            String tanggal = rs.getDate("tanggal").toLocalDate().format(fmtTanggal);
            String jam = rs.getTime("waktuMulai").toLocalTime().format(fmtJam)
                    + " - "
                    + rs.getTime("waktuSelesai").toLocalTime().format(fmtJam);

            String ruang = rs.getString("namaRuangan");
            if (ruang == null || ruang.isBlank())
                ruang = "-";

            return new AdminRiwayatBimbinganRow(
                    rs.getInt("idJadwal"),
                    rs.getString("namaMahasiswa"),
                    rs.getString("npm"),
                    rs.getString("namaDosen"),
                    rs.getString("nikDosen"),
                    tanggal,
                    jam,
                    ruang,
                    rs.getString("status"));
        });
    }

    @Override
    public Optional<AdminDetailBimbinganView> findDetailByIdJadwal(int idJadwal) {

        String sql = """
                    SELECT
                        jb.idJadwal,
                        um.name AS namaMahasiswa,
                        m.npm AS npm,
                        ud.name AS namaDosen,
                        d.nik AS nikDosen,
                        jb.tanggal,
                        jb.waktuMulai,
                        jb.waktuSelesai,
                        jb.status,
                        r.namaRuangan,
                        b.catatan,
                        b.fileURL
                    FROM jadwal_bimbingan jb
                    JOIN mahasiswa m ON m.idMhs = jb.idMhs
                    JOIN users um ON um.idUser = m.idMhs
                    JOIN dosen d ON d.idDosen = jb.idDosen
                    JOIN users ud ON ud.idUser = d.idDosen
                    LEFT JOIN ruangan r ON r.idRuangan = jb.idRuangan
                    LEFT JOIN bimbingan b ON b.idJadwal = jb.idJadwal
                    WHERE jb.idJadwal = ?
                    LIMIT 1
                """;

        List<AdminDetailBimbinganView> res = jdbc.query(sql, (rs, rowNum) -> {
            String tanggal = rs.getDate("tanggal").toLocalDate().format(fmtTanggal);
            String jam = rs.getTime("waktuMulai").toLocalTime().format(fmtJam)
                    + " - "
                    + rs.getTime("waktuSelesai").toLocalTime().format(fmtJam);

            String ruang = rs.getString("namaRuangan");
            if (ruang == null || ruang.isBlank())
                ruang = "-";

            return new AdminDetailBimbinganView(
                    rs.getInt("idJadwal"),
                    rs.getString("namaMahasiswa"),
                    rs.getString("npm"),
                    rs.getString("namaDosen"),
                    rs.getString("nikDosen"),
                    tanggal,
                    jam,
                    ruang,
                    rs.getString("status"),
                    rs.getString("catatan"), // boleh null
                    rs.getString("fileURL") // boleh null
            );
        }, idJadwal);

        return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
    }

}
