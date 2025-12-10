package com.example.tubes.Mahasiswa;

import com.example.tubes.Auth.User;
import com.example.tubes.JadwalBimbingin.JadwalBimbinganService;
import com.example.tubes.Notifikasi.NotifikasiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    @Autowired
    NotifikasiService notifikasiService;

    @Autowired
    private JadwalBimbinganService jadwalService;

    // Helper untuk cek login
    private User cekLogin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return null;
        }
        return user;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        // Ambil Data
        Mahasiswa mhsDetail = mahasiswaService.getMahasiswaByUserId(user.getId()).orElse(null);

        // Cek null pointer jika data belum lengkap
        if (mhsDetail != null) {
            Map<String, Object> topik = mahasiswaService.getTopikTA(mhsDetail.getIdMhs());
            Map<String, Object> nextBimbingan = mahasiswaService.getNextBimbingan(mhsDetail.getIdMhs());

            model.addAttribute("topik", topik);
            model.addAttribute("nextBimbingan", nextBimbingan);
            model.addAttribute("mhsDetail", mhsDetail);
        }

        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(user.getId()));
        return "Mahasiswa/dashboard-mhs";
    }

    @GetMapping("/jadwal")
    public String jadwal(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(user.getId()));
        // Nanti bisa tambah logic ambil data jadwal disini
        return "Mahasiswa/jadwal-mhs";
    }

    @GetMapping("/riwayat")
    public String riwayat(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(user.getId()));
        // Nanti bisa tambah logic ambil data riwayat disini
        return "Mahasiswa/riwayat-bimbingan";
    }

    @GetMapping("/pengajuan")
    public String pengajuan(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        int idMhs = user.getId();

        mahasiswaService.getNamaDosenPembimbing(idMhs).ifPresent(nama -> model.addAttribute("namaDosenPembimbing", nama));

        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(user.getId()));
        return "Mahasiswa/pengajuan-mhs";
    }

    @PostMapping("/ajukan-bimbingan")
    public String ajukanBimbingan(
            HttpSession session,
            @RequestParam("tanggal") String tanggal,
            @RequestParam("jamMulai") String jamMulai,
            @RequestParam("jamSelesai") String jamSelesai
    ) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idMhs = user.getId();

        // Cari dosen pembimbing mahasiswa
        Optional<String> namaDosen = mahasiswaService.getNamaDosenPembimbing(idMhs);

        // Dapatkan ID dosennya juga
        int idDosen = mahasiswaService.getIdDosenPembimbing(idMhs);

        // Simpan ke database
        jadwalService.insertPengajuan(
                idMhs,
                idDosen,
                LocalDate.parse(tanggal),
                LocalTime.parse(jamMulai),
                LocalTime.parse(jamSelesai)
        );

        // Kirim notifikasi ke dosen
        notifikasiService.buatNotif(idDosen, "Pengajuan Baru", "Mahasiswa mengajukan bimbingan baru.");

        return "redirect:/mahasiswa/pengajuan";
    }

}