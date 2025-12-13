package com.example.tubes.JadwalBimbingin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface JadwalBimbinganRepository {

    List<JadwalBimbingan> findPengajuanByDosen(int idDosen);

    void updateStatus(int idJadwal, String newStatus);

    List<JadwalBimbingan> findByMonth(int idDosen, int year, int month);

    void insertPengajuan(int idMhs, int idDosen, LocalDate tanggal, LocalTime mulai, LocalTime selesai);

    Optional<JadwalBimbingan> findById(int idJadwal);

    // ====== ADMIN ======
    List<JadwalBimbingan> findAll();                 // semua jadwal untuk admin
    void updateRuangan(int idJadwal, Integer idRuangan); // set ruangan (nullable)
        // ====== ADMIN (JOIN dosen + ruangan) ======
    List<AdminJadwalRow> findAllAdminRows();

}
