package com.example.tubes.JadwalBimbingin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tubes.Ruangan.Ruangan;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class JadwalBimbinganService {

    @Autowired
    private JadwalBimbinganRepository repo;

    public List<JadwalBimbingan> getPengajuanByDosen(int idDosen) {
        return repo.findPengajuanByDosen(idDosen);
    }

    public void setStatus(int idJadwal, String status) {
        repo.updateStatus(idJadwal, status);
    }

    public List<JadwalBimbingan> getByMonth(int idDosen, int year, int month) {
        return repo.findByMonth(idDosen, year, month);
    }

    public void insertPengajuan(int idMhs, int idDosen, LocalDate tanggal, LocalTime mulai, LocalTime selesai) {
        repo.insertPengajuan(idMhs, idDosen, tanggal, mulai, selesai);
    }

    public Optional<JadwalBimbingan> getById(int idJadwal) {
        return repo.findById(idJadwal);
    }

    public List<Ruangan> getAvailableRuangan(LocalDate tanggal, LocalTime mulai, LocalTime selesai){
        return repo.findAvailableRuangan(tanggal, mulai, selesai);
    }

    public void insertPengajuanDosen(int idMhs, int idDosen, int idRuangan, LocalDate tanggal, LocalTime mulai, LocalTime selesai, String status) {
        repo.insertPengajuanDosen(idMhs, idDosen, idRuangan, tanggal, mulai, selesai, status);
    }
}