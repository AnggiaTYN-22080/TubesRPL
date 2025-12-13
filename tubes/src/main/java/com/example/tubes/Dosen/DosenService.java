
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

    public Optional<Map<String, Object>> getJadwalMengajarById(int idDosen, int idJadwalKuliah) {
        return dosenRepository.findJadwalMengajarById(idDosen, idJadwalKuliah);
    }

    public int tambahJadwalMengajar(int idDosen, String hari, String jamMulai, String jamSelesai,
            String mataKuliah, String kelas) {
        return dosenRepository.tambahJadwalMengajar(idDosen, hari, jamMulai, jamSelesai, mataKuliah, kelas);
    }

    public void ubahJadwalMengajar(int idDosen, int idJadwalKuliah, String hari,
            String jamMulai, String jamSelesai,
            String mataKuliah, String kelas) {
        dosenRepository.ubahJadwalMengajar(idDosen, idJadwalKuliah, hari, jamMulai, jamSelesai, mataKuliah, kelas);
    }

    public void hapusJadwalMengajar(int idDosen, int idJadwalKuliah) {
        dosenRepository.hapusJadwalMengajar(idDosen, idJadwalKuliah);
    }

    public int[] importJadwalMengajarFromCSV(int idDosen, List<Map<String, String>> csvData) {
        int successCount = 0;
        int failCount = 0;

        for (Map<String, String> row : csvData) {
            try {
                // Handle various column name formats (case insensitive, with/without spaces)
                String hari = getValueIgnoreCase(row, "hari");
                String jamMulai = getValueIgnoreCase(row, "jammulai", "jam mulai");
                String jamSelesai = getValueIgnoreCase(row, "jamselesai", "jam selesai");
                String mataKuliah = getValueIgnoreCase(row, "matakuliah", "mata kuliah", "keterangan");
                String kelas = getValueIgnoreCase(row, "kelas");

                if (hari != null && !hari.isEmpty() &&
                        jamMulai != null && !jamMulai.isEmpty() &&
                        jamSelesai != null && !jamSelesai.isEmpty() &&
                        mataKuliah != null && !mataKuliah.isEmpty() &&
                        kelas != null && !kelas.isEmpty()) {
                    tambahJadwalMengajar(idDosen, hari, jamMulai, jamSelesai, mataKuliah, kelas);
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
            }
        }

        return new int[] { successCount, failCount };
    }

    private String getValueIgnoreCase(Map<String, String> map, String... keys) {
        for (String key : keys) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }
}
