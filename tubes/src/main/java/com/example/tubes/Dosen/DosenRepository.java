package com.example.tubes.Dosen;

import java.util.List;
import java.util.Optional;

public interface DosenRepository {
    List<Dosen> findAll();

    Optional<Dosen> findByNik(String nik);

    void save(Dosen dosen);

    void update(Dosen dosen);

    void delete(String nik);
}