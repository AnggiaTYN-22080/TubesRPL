package com.example.tubes.Periode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeriodeTAService {

    @Autowired
    private PeriodeTARepository repo;

    public List<PeriodeTA> getAll() {
        return repo.findAll();
    }

    public Optional<PeriodeTA> getById(int id) {
        return repo.findById(id);
    }

    public void tambah(PeriodeTA p) {
        repo.insert(p);
    }

    public void update(PeriodeTA p) {
        repo.update(p);
    }

    public void hapus(int id) {
        repo.delete(id);
    }
}
