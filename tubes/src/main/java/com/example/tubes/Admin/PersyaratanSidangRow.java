package com.example.tubes.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersyaratanSidangRow {
    private int idMhs;
    private String npm;
    private String namaMahasiswa;

    private int idTA;
    private String codeTA;
    private String topikTA;

    private String namaDosen;

    private String tahunAjaran;
    private int semester;

    private String statusPersyaratan; // dari penugasan_ta.StatusPersyaratan
}
