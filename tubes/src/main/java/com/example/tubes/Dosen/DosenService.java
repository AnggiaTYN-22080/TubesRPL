
package com.example.tubes.Dosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DosenService {

    @Autowired
    private DosenRepository dosenRepository;

    public Optional<Dosen> getDosenByUserId(int idUser) {
        return dosenRepository.findByUserId(idUser);
    }

    public int getJumlahMahasiswa(int idDosen) {
        return dosenRepository.countMahasiswaBimbingan(idDosen);
    }

    public int getTotalPengajuan(int idDosen) {
        return dosenRepository.countPengajuanBimbingan(idDosen);
    }
}
