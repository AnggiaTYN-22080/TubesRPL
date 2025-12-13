package com.example.tubes.Ruangan;

import java.util.List;
import java.util.Optional;

public interface RuanganRepository {
    List<Ruangan> findAll();
    Optional<Ruangan> findById(int idRuangan);
    int insert(String namaRuangan, String statusRuangan);
    boolean update(int idRuangan, String namaRuangan, String statusRuangan);
    boolean delete(int idRuangan);
}
