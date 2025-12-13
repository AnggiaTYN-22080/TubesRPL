package com.example.tubes.Periode;

public class PeriodeForm {
    private String nama;          // input user (contoh: "TA1 ganjil 2025")
    private String tanggalMulai;  // input text
    private String tanggalSelesai;

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(String tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public String getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(String tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
}
