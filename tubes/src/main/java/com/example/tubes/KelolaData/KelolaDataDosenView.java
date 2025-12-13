package com.example.tubes.KelolaData;

public class KelolaDataDosenView {
    private int idUser;
    private String nama;
    private String email;
    private String nik;

    public KelolaDataDosenView() {}

    public KelolaDataDosenView(int idUser, String nama, String email, String nik) {
        this.idUser = idUser;
        this.nama = nama;
        this.email = email;
        this.nik = nik;
    }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }
}
