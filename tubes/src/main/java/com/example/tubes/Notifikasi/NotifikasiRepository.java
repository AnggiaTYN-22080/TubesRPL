package com.example.tubes.Notifikasi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotifikasiRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void insertNotif(int idUser, String tipe, String keterangan) {
        String sql = """
                    INSERT INTO notifikasi (idUser, tipeNotif, keterangan)
                    VALUES (?, ?, ?)
                """;
        jdbc.update(sql, idUser, tipe, keterangan);
    }

    public List<Notifikasi> getNotifByUser(int idUser) {
        String sql = """
                    SELECT * FROM notifikasi
                    WHERE idUser = ?
                    ORDER BY waktu DESC
                """;

        return jdbc.query(sql, (rs, rowNum) -> {
            Notifikasi n = new Notifikasi();
            n.setIdNotif(rs.getInt("idNotif"));
            n.setIdUser(rs.getInt("idUser"));
            n.setTipeNotif(rs.getString("tipeNotif"));
            n.setKeterangan(rs.getString("keterangan"));
            n.setWaktu(rs.getString("waktu"));
            return n;
        }, idUser);
    }

    public void deleteNotif(int idNotif) {
        jdbc.update("DELETE FROM notifikasi WHERE idNotif = ?", idNotif);
    }
}
