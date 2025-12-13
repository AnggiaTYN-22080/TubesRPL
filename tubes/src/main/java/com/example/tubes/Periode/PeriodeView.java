package com.example.tubes.Periode;

import java.time.LocalDate;

public class PeriodeView {
    private int idPeriodeTA;
    private String nama;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;

    public PeriodeView(int idPeriodeTA, String nama, LocalDate tanggalMulai, LocalDate tanggalSelesai) {
        this.idPeriodeTA = idPeriodeTA;
        this.nama = nama;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
    }

    public int getIdPeriodeTA() { return idPeriodeTA; }
    public String getNama() { return nama; }
    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
}
