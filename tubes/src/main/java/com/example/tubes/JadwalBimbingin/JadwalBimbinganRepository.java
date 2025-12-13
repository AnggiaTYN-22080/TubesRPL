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

    List<JadwalBimbingan> findByMahasiswa(int idMhs);

    void insertPengajuan(int idMhs, int idDosen, LocalDate tanggal, LocalTime mulai, LocalTime selesai, int idRuangan);
}