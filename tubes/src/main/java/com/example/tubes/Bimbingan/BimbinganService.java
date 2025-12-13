package com.example.tubes.Bimbingan;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class BimbinganService {

    @Autowired
    private BimbinganRepository repo;

    public Optional<Bimbingan> getByJadwal(int idJadwal) {
        return repo.findByJadwal(idJadwal);
    }

    public void simpanCatatan(int idJadwal, String catatan) {
        repo.saveOrUpdate(idJadwal, catatan);
    }
}
