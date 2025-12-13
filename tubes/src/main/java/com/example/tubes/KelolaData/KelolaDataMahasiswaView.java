package com.example.tubes.KelolaData;

public class KelolaDataMahasiswaView {
    private int idUser;
    private String nama;
    private String email;
    private String npm;

    public KelolaDataMahasiswaView() {}

    public KelolaDataMahasiswaView(int idUser, String nama, String email, String npm) {
        this.idUser = idUser;
        this.nama = nama;
        this.email = email;
        this.npm = npm;
    }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNpm() { return npm; }
    public void setNpm(String npm) { this.npm = npm; }
}
