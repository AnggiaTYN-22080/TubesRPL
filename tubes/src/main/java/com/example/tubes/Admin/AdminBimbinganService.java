package com.example.tubes.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminBimbinganService {

    @Autowired
    private AdminBimbinganRepository repo;

    public List<AdminRiwayatBimbinganRow> getRiwayat() {
        return repo.findRiwayatAll();
    }

    public Optional<AdminDetailBimbinganView> getDetail(int idJadwal) {
        return repo.findDetailByIdJadwal(idJadwal);
    }
}
