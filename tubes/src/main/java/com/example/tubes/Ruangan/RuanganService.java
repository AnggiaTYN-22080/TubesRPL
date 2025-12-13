package com.example.tubes.Ruangan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuanganService {

    @Autowired
    private RuanganRepository repo;

    public List<Ruangan> getAll() {
        return repo.findAll();
    }

    public Optional<Ruangan> getById(int idRuangan) {
        return repo.findById(idRuangan);
    }

    public int create(String namaRuangan, String statusRuangan) {
        return repo.insert(namaRuangan, statusRuangan);
    }

    public boolean update(int idRuangan, String namaRuangan, String statusRuangan) {
        return repo.update(idRuangan, namaRuangan, statusRuangan);
    }

    public boolean delete(int idRuangan) {
        return repo.delete(idRuangan);
    }
}
