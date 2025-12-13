package com.example.tubes.Periode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Periode {
    private Integer id;           // kalau di DB kamu ada id periode
    private String nama;          // contoh: "TA1 Ganjil 2025"
    private String tanggalMulai;  // sementara String dulu biar gampang cocok sama input
    private String tanggalSelesai;
}
