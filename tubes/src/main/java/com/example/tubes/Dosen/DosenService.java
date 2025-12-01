package com.example.tubes.Dosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DosenService {
    @Autowired
    private DosenRepository dosenRepository; // Sebaiknya private

    public List<Dosen> getAllDosen() {
        return dosenRepository.findAll();
    }

    // TAMBAHKAN INI (Wrapper Method)
    public Optional<Dosen> getDosenByNik(String nik) {
        return dosenRepository.findByNik(nik);
    }
}