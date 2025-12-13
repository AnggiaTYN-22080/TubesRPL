package com.example.tubes.JadwalBimbingin;

import com.example.tubes.Ruangan.Ruangan;
import com.example.tubes.Ruangan.RuanganService;
import com.example.tubes.Dosen.Dosen;
import com.example.tubes.Dosen.DosenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JadwalBimbinganService {

    @Autowired
    private JadwalBimbinganRepository repo;

    // dipakai untuk format nama dosen & ruangan
    @Autowired
    private DosenService dosenService;

    @Autowired
    private RuanganService ruanganService;

    public List<JadwalBimbingan> getPengajuanByDosen(int idDosen) {
        return repo.findPengajuanByDosen(idDosen);
    }

    public void updateStatus(int idJadwal, String status) {
        repo.updateStatus(idJadwal, status);
    }

    public List<JadwalBimbingan> getByMonth(int idDosen, int year, int month) {
        return repo.findByMonth(idDosen, year, month);
    }

    public void insertPengajuan(int idMhs, int idDosen, java.time.LocalDate tanggal,
                                java.time.LocalTime mulai, java.time.LocalTime selesai) {
        repo.insertPengajuan(idMhs, idDosen, tanggal, mulai, selesai);
    }

    public Optional<JadwalBimbingan> getById(int idJadwal) {
        return repo.findById(idJadwal);
    }

        // =====================
    // ADMIN
    // =====================
    public List<JadwalRuanganView> getAllForAdmin() {
        Locale idLocale = new Locale("id", "ID");
        DateTimeFormatter fmtTanggal = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", idLocale);
        DateTimeFormatter fmtJam = DateTimeFormatter.ofPattern("HH.mm");

        return repo.findAllAdminRows().stream().map(row -> {
            String dosenText = (row.getNikDosen() + " " + row.getNamaDosen()).trim();
            String tanggalText = row.getTanggal().format(fmtTanggal);
            String jamText = row.getWaktuMulai().format(fmtJam) + " - " + row.getWaktuSelesai().format(fmtJam);

            String ruangText = (row.getNamaRuangan() == null || row.getNamaRuangan().isBlank())
                    ? "-"
                    : row.getNamaRuangan();

            return new JadwalRuanganView(dosenText, tanggalText, jamText, ruangText);
        }).toList();
    }


    public boolean updateRuanganIfExists(int idJadwal, Integer idRuangan) {
        if (repo.findById(idJadwal).isEmpty()) return false;
        repo.updateRuangan(idJadwal, idRuangan);
        return true;
    }

    public boolean updateStatusIfExists(int idJadwal, String status) {
        if (repo.findById(idJadwal).isEmpty()) return false;
        repo.updateStatus(idJadwal, status);
        return true;
    }

    public void setStatus(int idJadwal, String status) {
    repo.updateStatus(idJadwal, status);
}

}
