package com.example.tubes.JadwalBimbingin;

import java.util.List;

public interface JadwalBimbinganRepository {

    List<JadwalBimbingan> findPengajuanByDosen(int idDosen);

    void updateStatus(int idJadwal, String newStatus);

    List<JadwalBimbingan> findByMonth(int idDosen, int year, int month);
}