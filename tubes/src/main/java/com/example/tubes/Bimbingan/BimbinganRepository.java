package com.example.tubes.Bimbingan;

import java.util.Optional;

public interface BimbinganRepository {

    Optional<Bimbingan> findByJadwal(int idJadwal);

    void saveOrUpdate(int idJadwal, String catatan);
}
