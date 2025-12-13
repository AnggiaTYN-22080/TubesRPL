
package com.example.tubes.Dosen;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.tubes.JadwalBimbingin.JadwalBimbingan;
import com.example.tubes.Mahasiswa.MahasiswaBimbingan;

public interface DosenRepository {
        Optional<Dosen> findByUserId(int idUser);

        int countMahasiswaBimbingan(int idDosen);

        int countPengajuanBimbingan(int idDosen);

        List<MahasiswaBimbingan> findMahasiswaBimbingan(int idDosen);

        List<JadwalBimbingan> findRiwayatBimbinganMahasiswa(int idMhs);

        List<Map<String, Object>> findJadwalMengajar(int idDosen);

        Optional<Map<String, Object>> findJadwalMengajarById(int idDosen, int idJadwalKuliah);

        int tambahJadwalMengajar(int idDosen, String hari, String jamMulai, String jamSelesai,
                        String mataKuliah, String kelas);

        void ubahJadwalMengajar(int idDosen, int idJadwalKuliah, String hari,
                        String jamMulai, String jamSelesai,
                        String mataKuliah, String kelas);

        void hapusJadwalMengajar(int idDosen, int idJadwalKuliah);
}
