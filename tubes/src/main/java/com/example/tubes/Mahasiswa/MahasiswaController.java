package com.example.tubes.Mahasiswa;

import com.example.tubes.Auth.User;
import com.example.tubes.JadwalBimbingin.JadwalBimbingan;
import com.example.tubes.JadwalBimbingin.JadwalBimbinganService;
import com.example.tubes.JadwalKuliah.JadwalKuliah;
import com.example.tubes.JadwalKuliah.JadwalKuliahService;
import com.example.tubes.Notifikasi.NotifikasiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.tubes.Ruangan.Ruangan;
import com.example.tubes.Ruangan.RuanganService;

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

    @Autowired
    private JadwalKuliahService jadwalKuliahService;

    @Autowired
    private RuanganService ruanganService;

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

    // jadwal bimbingan calender
    @GetMapping("/jadwal-bimbingan")
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

    @GetMapping("/riwayat")
    public String riwayat(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        int idMhs = user.getId();

        List<JadwalBimbingan> riwayatList = jadwalService.getJadwalByMahasiswa(idMhs);

        model.addAttribute("riwayatList", riwayatList);
        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(idMhs));

        return "Mahasiswa/riwayat-bimbingan";
    }

    @GetMapping("/pengajuan")
    public String pengajuan(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        int idMhs = user.getId();

        mahasiswaService.getNamaDosenPembimbing(idMhs)
                .ifPresent(nama -> model.addAttribute("namaDosenPembimbing", nama));

        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(user.getId()));
        return "Mahasiswa/pengajuan-mhs";
    }

    @GetMapping("/jadwal")
    public String jadwalKuliah(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("notifList", notifikasiService.getNotifByUser(user.getId()));

        List<JadwalKuliah> listJadwal = jadwalKuliahService.getByMhs(user.getId());
        model.addAttribute("listJadwal", listJadwal);

        return "Mahasiswa/jadwal";
    }

    @GetMapping("/api/ruangan-tersedia")
    @ResponseBody
    public List<Ruangan> getRuanganTersedia(
            @RequestParam("tanggal") LocalDate tanggal,
            @RequestParam("jamMulai") LocalTime jamMulai,
            @RequestParam("jamSelesai") LocalTime jamSelesai) {
        return ruanganService.cariRuanganKosong(tanggal, jamMulai, jamSelesai);
    }

    @PostMapping("/ajukan-bimbingan")
    public String prosesPengajuan(
            HttpSession session,
            @RequestParam("tanggal") LocalDate tanggal,
            @RequestParam("jamMulai") LocalTime jamMulai,
            @RequestParam("jamSelesai") LocalTime jamSelesai,
            @RequestParam("idRuangan") int idRuangan) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        int idMhs = user.getId();
        int idDosen = mahasiswaService.getIdDosenPembimbing(idMhs);

        // Update method insertPengajuan di Service Anda untuk menerima idRuangan juga
        jadwalService.insertPengajuan(idMhs, idDosen, tanggal, jamMulai, jamSelesai, idRuangan);

        notifikasiService.buatNotif(idDosen, "Pengajuan Bimbingan",
                "Mahasiswa " + user.getName() + " mengajukan jadwal baru.");

        return "redirect:/mahasiswa/riwayat";
    }
}