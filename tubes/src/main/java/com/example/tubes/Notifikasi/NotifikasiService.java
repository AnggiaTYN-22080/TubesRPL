package com.example.tubes.Notifikasi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotifikasiService {

    @Autowired
    private NotifikasiRepository repo;

    public void buatNotif(int idUser, String tipe, String keterangan) {
        repo.insertNotif(idUser, tipe, keterangan);
    }

    public List<Notifikasi> getNotifByUser(int idUser) {
        return repo.getNotifByUser(idUser);
    }

    public void hapusNotif(int idNotif) {
        repo.deleteNotif(idNotif);
    }
}
