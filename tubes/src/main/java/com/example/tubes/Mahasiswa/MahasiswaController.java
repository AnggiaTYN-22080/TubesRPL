package com.example.tubes.Mahasiswa;

import com.example.tubes.Auth.User;
import com.example.tubes.JadwalBimbingin.JadwalBimbingan;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    @Autowired
    private NotifikasiService notifikasiService;

    @Autowired
    private JadwalBimbinganService jadwalService;

    // --- HELPER: Cek Login & Role ---
    private User cekLogin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return null;
        }
        return user;
    }

    // --- 1. DASHBOARD ---
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        // Ambil Data Mahasiswa
        Mahasiswa mhsDetail = mahasiswaService.getMahasiswaByUserId(user.getId()).orElse(null);

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

    // --- 2. JADWAL KULIAH (TAMPILAN LIST) ---
    @GetMapping("/jadwal")
    public String jadwalKuliah(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(user.getId()));
        // model.addAttribute("jadwalKuliah", ...);

        return "Mahasiswa/jadwal";
    }

    // --- 3. JADWAL BIMBINGAN (TAMPILAN KALENDER) ---
    @GetMapping("/jadwal-bimbingan") // Sesuai href di HTML
    public String jadwalBimbingan(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        int idMhs = user.getId();

        // Mengambil semua jadwal bimbingan (Approved/Pending)
        // Method 'getJadwalByMahasiswa' ada di Service JadwalBimbingan (Lihat poin 6 di
        // bawah)
        List<JadwalBimbingan> daftarJadwal = jadwalService.getJadwalByMahasiswa(idMhs);

        model.addAttribute("daftarJadwal", daftarJadwal);
        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(idMhs));

        return "Mahasiswa/jadwal-mhs";
    }

    // --- 4. RIWAYAT BIMBINGAN ---
    @GetMapping("/riwayat")
    public String riwayat(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        int idMhs = user.getId();

        // Menggunakan data yang sama, tapi nanti di HTML ditampilkan sebagai tabel
        List<JadwalBimbingan> riwayatList = jadwalService.getJadwalByMahasiswa(idMhs);

        model.addAttribute("riwayatList", riwayatList);
        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(idMhs));

        return "Mahasiswa/riwayat-bimbingan";
    }

    // --- 5. HALAMAN PENGAJUAN ---
    @GetMapping("/pengajuan")
    public String pengajuan(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        int idMhs = user.getId();

        // Tampilkan nama dosen pembimbing agar mahasiswa tahu mengajukan ke siapa
        mahasiswaService.getNamaDosenPembimbing(idMhs)
                .ifPresent(nama -> model.addAttribute("namaDosenPembimbing", nama));

        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(user.getId()));
        return "Mahasiswa/pengajuan-mhs";
    }

    // --- 6. PROSES SUBMIT PENGAJUAN ---
    @PostMapping("/ajukan-bimbingan")
    public String ajukanBimbingan(
            HttpSession session,
            @RequestParam("tanggal") String tanggal,
            @RequestParam("jamMulai") String jamMulai,
            @RequestParam("jamSelesai") String jamSelesai,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idMhs = user.getId();

        // Cari ID Dosen Pembimbing secara otomatis
        int idDosen = mahasiswaService.getIdDosenPembimbing(idMhs);

        // Simpan ke Database
        jadwalService.insertPengajuan(
                idMhs,
                idDosen,
                LocalDate.parse(tanggal),
                LocalTime.parse(jamMulai),
                LocalTime.parse(jamSelesai));

        // Kirim Notifikasi ke Dosen
        notifikasiService.buatNotif(idDosen, "Pengajuan Baru", "Mahasiswa mengajukan bimbingan baru.");

        redirectAttributes.addFlashAttribute("successMessage", "Pengajuan bimbingan berhasil dikirim");

        return "redirect:/mahasiswa/pengajuan";
    }
}