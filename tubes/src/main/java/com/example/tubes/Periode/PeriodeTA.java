package com.example.tubes.Periode;

import java.time.LocalDate;

public class PeriodeTA {
    private int idPeriodeTA;
    private String tahunAjaran;
    private int semester;
    private LocalDate tanggalMulaiPeriodeTA;
    private LocalDate tanggalSelesaiPeriodeTA;

    public int getIdPeriodeTA() { return idPeriodeTA; }
    public void setIdPeriodeTA(int idPeriodeTA) { this.idPeriodeTA = idPeriodeTA; }

    public String getTahunAjaran() { return tahunAjaran; }
    public void setTahunAjaran(String tahunAjaran) { this.tahunAjaran = tahunAjaran; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public LocalDate getTanggalMulaiPeriodeTA() { return tanggalMulaiPeriodeTA; }
    public void setTanggalMulaiPeriodeTA(LocalDate tanggalMulaiPeriodeTA) { this.tanggalMulaiPeriodeTA = tanggalMulaiPeriodeTA; }

    public LocalDate getTanggalSelesaiPeriodeTA() { return tanggalSelesaiPeriodeTA; }
    public void setTanggalSelesaiPeriodeTA(LocalDate tanggalSelesaiPeriodeTA) { this.tanggalSelesaiPeriodeTA = tanggalSelesaiPeriodeTA; }
}
