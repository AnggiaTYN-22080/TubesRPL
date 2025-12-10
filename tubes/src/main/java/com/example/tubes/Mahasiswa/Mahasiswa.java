package com.example.tubes.Mahasiswa;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mahasiswa {
    // Dari tabel mahasiswa
    private int idMhs; // FK untuk ke users
    private String npm;

    // Dari tabel users
    private String nama;
    private String email;
    private String password;
    private String role;
}