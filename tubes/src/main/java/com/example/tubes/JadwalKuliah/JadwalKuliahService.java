package com.example.tubes.JadwalKuliah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JadwalKuliahService {

    @Autowired
    private JadwalKuliahRepository repo;

    public List<JadwalKuliah> getByMhs(int idMhs) {
        return repo.findByMhs(idMhs);
    }

    public void save(int idMhs, JadwalKuliah jk) {
        repo.saveCustom(idMhs, jk);
    }

    // Tambahkan method ini
    public void hapusJadwal(int idJadwalKuliah) {
        repo.delete(idJadwalKuliah);
    }
}