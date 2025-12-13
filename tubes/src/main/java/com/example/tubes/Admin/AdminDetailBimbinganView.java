package com.example.tubes.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDetailBimbinganView {
    private int idJadwal;

    private String namaMahasiswa;
    private String npm;

    private String namaDosen;
    private String nikDosen;

    private String tanggal;   // format indo
    private String jam;       // format jam
    private String ruangan;   // "-" jika kosong
    private String status;

    private String catatan;   // dari bimbingan
    private String fileURL;   // dari bimbingan
}
