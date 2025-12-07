package com.example.tubes.Mahasiswa;

import java.util.Optional;

public interface MahasiswaRepository {
    Optional<Mahasiswa> findByUserId(int idUser);
}