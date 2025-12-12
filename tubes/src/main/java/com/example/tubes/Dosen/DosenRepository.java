
package com.example.tubes.Dosen;

import java.util.List;
import java.util.Optional;

import com.example.tubes.Mahasiswa.MahasiswaBimbingan;

public interface DosenRepository {
    Optional<Dosen> findByUserId(int idUser);

    int countMahasiswaBimbingan(int idDosen);

    int countPengajuanBimbingan(int idDosen);

    List<MahasiswaBimbingan> findMahasiswaBimbingan(int idDosen);
}
