package com.example.tubes.JadwalBimbingin;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class JadwalBimbingan {

    private int idJadwal;
    private LocalDate tanggal;
    private LocalTime waktuMulai;
    private LocalTime waktuSelesai;
    private String status;

    private int idMhs;
    private int idDosen;
    private int idRuangan;
    private String namaRuangan;
    private String namaMahasiswa;
    private String npm;
    
    // Untuk kalender
    private String topikTA;
}