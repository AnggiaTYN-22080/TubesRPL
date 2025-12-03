package com.example.tubes.JadwalBimbingin;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/jadwal")
public class JadwalBimbinganController {

    @Autowired
    private JadwalBimbinganRepository jadwalBimbinganRepository;

    @GetMapping("/kelola")
    public String kelolaJadwal(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"DOSEN".equals(user.getRole())) {
            return "redirect:/login";
        }

        // 1. Logika Kalender (Default: Bulan Sekarang)
        LocalDate now = LocalDate.now();
        YearMonth yearMonth = YearMonth.from(now);
        int daysInMonth = yearMonth.lengthOfMonth();

        // Cari tahu tanggal 1 hari apa (1=Senin, 7=Minggu)
        // Kita mau kalender mulai Minggu, jadi sesuaikan offsetnya
        int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();
        int blankDays = (firstDayOfWeek == 7) ? 0 : firstDayOfWeek; // Jika tgl 1 Minggu, blank 0. Jika Senin, blank 1.

        // Buat List tanggal kosong untuk kotak abu-abu di awal bulan
        List<Integer> blanks = new ArrayList<>();
        for (int i = 0; i < blankDays; i++)
            blanks.add(i);

        // Buat List tanggal 1-30/31
        List<Integer> dates = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++)
            dates.add(i);

        // 2. Ambil Data Database
        List<JadwalBimbingan> listJadwal = jadwalBimbinganRepository.findByDosenId(user.getIdUser());
        List<Map<String, Object>> listRuangan = jadwalBimbinganRepository.findAllRuangan();
        List<Map<String, Object>> listMahasiswa = jadwalBimbinganRepository.findMahasiswaByDosen(user.getIdUser());

        model.addAttribute("currentMonth", now.getMonth().name());
        model.addAttribute("currentYear", now.getYear());
        model.addAttribute("blanks", blanks);
        model.addAttribute("dates", dates);

        model.addAttribute("listJadwal", listJadwal);
        model.addAttribute("listRuangan", listRuangan);
        model.addAttribute("listMahasiswa", listMahasiswa);
        model.addAttribute("user", user);

        return "Dosen/kelola-jadwal";
    }

    @PostMapping("/tambah")
    public String tambahJadwal(
            @RequestParam("tanggal") String tanggal,
            @RequestParam("jamMulai") String jamMulai,
            @RequestParam("jamSelesai") String jamSelesai,
            @RequestParam("ruanganId") int ruanganId,
            @RequestParam("mahasiswaId") String mahasiswaId,
            HttpSession session) {

        User user = (User) session.getAttribute("currentUser");

        LocalDateTime start = LocalDateTime.parse(tanggal + "T" + jamMulai);
        LocalDateTime end = LocalDateTime.parse(tanggal + "T" + jamSelesai);

        JadwalBimbingan jadwalBaru = new JadwalBimbingan();
        jadwalBaru.setDosenId(user.getIdUser());
        jadwalBaru.setWaktuMulai(start);
        jadwalBaru.setWaktuSelesai(end);

        // Simpan dengan Mahasiswa dan Ruangan
        jadwalBimbinganRepository.save(jadwalBaru, mahasiswaId, ruanganId);

        return "redirect:/jadwal/kelola";
    }
}