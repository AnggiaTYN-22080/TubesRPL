package com.example.tubes.Mahasiswa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MahasiswaService {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    // Method untuk mengambil data detail mahasiswa berdasarkan ID User (dari login)
    public Optional<Mahasiswa> getMahasiswaByUserId(int userId) {
        return mahasiswaRepository.findByUserId(userId);
    }
}