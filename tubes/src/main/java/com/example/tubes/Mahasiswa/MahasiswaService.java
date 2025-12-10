package com.example.tubes.Mahasiswa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MahasiswaService {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    public Optional<Mahasiswa> getMahasiswaByUserId(int userId) {
        return mahasiswaRepository.findByUserId(userId);
    }

    public Map<String, Object> getTopikTA(int idMhs) {
        return mahasiswaRepository.findTopikTA(idMhs);
    }

    public Map<String, Object> getNextBimbingan(int idMhs) {
        return mahasiswaRepository.findNextBimbingan(idMhs);
    }

    public Optional<String> getNamaDosenPembimbing(int idMhs) {
        return mahasiswaRepository.findNamaDosenPembimbing(idMhs);
    }

    public int getIdDosenPembimbing(int idMhs) {
        return mahasiswaRepository.findIdDosenPembimbing(idMhs).orElse(0);
    }
}