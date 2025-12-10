<<<<<<< HEAD
=======
package com.example.tubes.JadwalBimbingin;

import java.util.List;

public interface JadwalBimbinganRepository {

    List<JadwalBimbingan> findPengajuanByDosen(int idDosen);

    void updateStatus(int idJadwal, String newStatus);

    List<JadwalBimbingan> findByMonth(int idDosen, int year, int month);
}
>>>>>>> 64dada559d9be840697add6885afae55c39ee148
