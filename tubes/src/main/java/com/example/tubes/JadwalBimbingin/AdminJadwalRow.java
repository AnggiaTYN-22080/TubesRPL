package com.example.tubes.JadwalBimbingin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AdminJadwalRow {
    private int idJadwal;
    private String status;

    private LocalDate tanggal;
    private LocalTime waktuMulai;
    private LocalTime waktuSelesai;

    private String nikDosen;
    private String namaDosen;

    private Integer idRuangan;      // nullable
    private String namaRuangan;     // nullable
}
