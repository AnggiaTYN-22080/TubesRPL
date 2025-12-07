package com.example.tubes.Dosen;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dosen {
    // Dari tabel dosen
    private int idDosen; // FK ke users
    private String nik;

    // Dari tabel users
    private String nama;
    private String email;
}