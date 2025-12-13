package com.example.tubes.KelolaData.dto;

public class TopikRow {
    private int id;          // idMhs
    private String nama;     // users.nama
    private String npm;      // mahasiswa.npm
    private String kodeTa;   // ta.codeTA
    private Integer dosenId; // ta.idDosen
    private String dosenNama;// users.nama dosen
    private String topik;    // ta.topikTA

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNpm() { return npm; }
    public void setNpm(String npm) { this.npm = npm; }

    public String getKodeTa() { return kodeTa; }
    public void setKodeTa(String kodeTa) { this.kodeTa = kodeTa; }

    public Integer getDosenId() { return dosenId; }
    public void setDosenId(Integer dosenId) { this.dosenId = dosenId; }

    public String getDosenNama() { return dosenNama; }
    public void setDosenNama(String dosenNama) { this.dosenNama = dosenNama; }

    public String getTopik() { return topik; }
    public void setTopik(String topik) { this.topik = topik; }
}
