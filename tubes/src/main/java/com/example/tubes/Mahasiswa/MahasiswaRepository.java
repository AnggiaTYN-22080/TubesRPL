package com.example.tubes.Mahasiswa;

import java.util.Map;
import java.util.Optional;

public interface MahasiswaRepository {
    Optional<Mahasiswa> findByUserId(int idUser);

    Map<String, Object> findTopikTA(int idMhs);

    Map<String, Object> findNextBimbingan(int idMhs);
}