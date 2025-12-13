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

    // Dipakai untuk halaman pengajuan (Fitur Dosen)
    private String namaRuangan;
    private String namaMahasiswa;
    private String npm;

    // Untuk kalender (Fitur Dosen)
    private String topikTA;

    // Untuk kalender (Fitur Mahasiswa)
    private String namaDosen;

}