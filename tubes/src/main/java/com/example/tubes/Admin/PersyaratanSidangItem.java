package com.example.tubes.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersyaratanSidangItem {
    private int idMhs;
    private int idTA;

    private String nama;        // dipakai di HTML: ${syarat.nama}
    private boolean terpenuhi;  // dipakai di HTML: ${syarat.terpenuhi}
    private String labelStatus; // dipakai di HTML: ${syarat.labelStatus}

    // opsional (kalau nanti mau tampilkan)
    private String email;
    private String npm;
}
