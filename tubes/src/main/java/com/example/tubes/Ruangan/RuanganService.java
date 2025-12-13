package com.example.tubes.Ruangan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class RuanganService {

    @Autowired
    private RuanganRepository repo;

    public List<Ruangan> cariRuanganKosong(LocalDate tanggal, LocalTime mulai, LocalTime selesai) {
        return repo.findAvailableRooms(tanggal, mulai, selesai);
    }
}