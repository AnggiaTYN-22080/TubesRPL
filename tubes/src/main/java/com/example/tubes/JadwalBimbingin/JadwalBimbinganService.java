<<<<<<< HEAD
=======
package com.example.tubes.JadwalBimbingin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JadwalBimbinganService {

    @Autowired
    private JadwalBimbinganRepository repo;

    public List<JadwalBimbingan> getPengajuanByDosen(int idDosen) {
        return repo.findPengajuanByDosen(idDosen);
    }

    public void setStatus(int idJadwal, String status) {
        repo.updateStatus(idJadwal, status);
    }

    public List<JadwalBimbingan> getByMonth(int idDosen, int year, int month) {
        return repo.findByMonth(idDosen, year, month);
    }
}
>>>>>>> 64dada559d9be840697add6885afae55c39ee148
