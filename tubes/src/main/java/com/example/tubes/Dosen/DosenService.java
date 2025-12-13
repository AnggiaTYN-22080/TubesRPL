
package com.example.tubes.Dosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tubes.JadwalBimbingin.JadwalBimbingan;
import com.example.tubes.Mahasiswa.MahasiswaBimbingan;

import java.util.List;
import java.util.Map;
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

    public List<MahasiswaBimbingan> getMahasiswaBimbingan(int idDosen) {
        return dosenRepository.findMahasiswaBimbingan(idDosen);
    }

    public List<JadwalBimbingan> getRiwayatBimbinganMahasiswa(int idMhs) {
        return dosenRepository.findRiwayatBimbinganMahasiswa(idMhs);
    }

    public List<Map<String, Object>> getJadwalMengajar(int idDosen) {
        return dosenRepository.findJadwalMengajar(idDosen);
    }
}
