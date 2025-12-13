package com.example.tubes.Ruangan;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RuanganRepository {
    List<Ruangan> findAvailableRooms(LocalDate tanggal, LocalTime jamMulai, LocalTime jamSelesai);
}