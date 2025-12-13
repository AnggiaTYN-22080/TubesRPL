package com.example.tubes.JadwalKuliah;

import lombok.Data;
import java.time.LocalTime;

@Data
public class JadwalKuliah {
    private int idJadwalKuliah;
    private String hari;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String kelas;
    private String keterangan;
}