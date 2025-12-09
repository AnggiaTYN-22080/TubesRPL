package com.example.tubes.Notifikasi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notifikasi {

    private int idNotif;
    private int idUser;
    private String tipeNotif;
    private String keterangan;
    private String waktu;
}
