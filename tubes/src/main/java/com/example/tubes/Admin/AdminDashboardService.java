package com.example.tubes.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tubes.Notifikasi.Notifikasi;
import com.example.tubes.Notifikasi.NotifikasiRepository;

import java.util.List;

@Service
public class AdminDashboardService {

    @Autowired
    private AdminDashboardRepository repo;

    @Autowired
    private NotifikasiRepository notifikasiRepository;

    public int getTotalMahasiswa() {
        return repo.countMahasiswa();
    }

    public int getTotalDosenPembimbing() {
        return repo.countDosen();
    }

    public int getPengajuanAktif() {
        return repo.countPengajuanAktif();
    }

    public List<Notifikasi> getNotifList(int idUser) {
        // pakai yang sudah ada di project kamu
        return notifikasiRepository.getNotifByUser(idUser);
    }
}
