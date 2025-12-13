package com.example.tubes.JadwalKuliah;

import java.util.List;

public interface JadwalKuliahRepository {
    List<JadwalKuliah> findByMhs(int idMhs);

    void saveCustom(int idMhs, JadwalKuliah jk);
}