package com.example.tubes.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminRiwayatBimbinganRow {
    private int idJadwal;

    private String namaMahasiswa;
    private String npm;

    private String namaDosen;
    private String nikDosen;

    private String tanggal;   // sudah diformat
    private String jam;       // sudah diformat

    private String ruangan;   // "-" kalau null
    private String status;    // done/approved/pending, dll
}
