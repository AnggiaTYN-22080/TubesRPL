package com.example.tubes.Dosen;

import java.util.Optional;

public interface DosenRepository {
    Optional<Dosen> findByUserId(int idUser);
    int countMahasiswaBimbingan(int idDosen);
    int countPengajuanBimbingan(int idDosen);
}