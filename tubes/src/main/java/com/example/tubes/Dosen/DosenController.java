package com.example.tubes.Dosen;
import com.example.tubes.Auth.User;
import com.example.tubes.Mahasiswa.MahasiswaService;
import com.example.tubes.Notifikasi.Notifikasi;
import com.example.tubes.Notifikasi.NotifikasiService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dosen")
public class DosenController {

    @Autowired
    private DosenService dosenService;

    @Autowired
    private NotifikasiService notifService;

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        // Ambil notifikasi
        List<Notifikasi> notifList = notifService.getNotifByUser(idDosen);
        model.addAttribute("notifList", notifList);

        // Ambil jumlah mahasiswa bimbingan
        int jumlahMahasiswa = dosenService.getJumlahMahasiswa(idDosen);
        model.addAttribute("jumlahMahasiswa", jumlahMahasiswa);

        // Ambil jumlah pengajuan pending
        int totalPengajuan = dosenService.getTotalPengajuan(idDosen);
        model.addAttribute("totalPengajuan", totalPengajuan);

        // Detail user dosen
        model.addAttribute("user", user);
        model.addAttribute("dosenDetail", dosenService.getDosenByUserId(idDosen).orElse(null));

        return "Dosen/DashboardDosen";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/daftar-mahasiswa")
    public String daftarMahasiswa(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();
        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));

        model.addAttribute("mahasiswaList", dosenService.getMahasiswaBimbingan(idDosen));

        return "Dosen/daftar-mhs";
    }

    @GetMapping("/mahasiswa/detail/{idMhs}")
    public String detailMahasiswa(
            @PathVariable int idMhs,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));
        model.addAttribute("mhs", mahasiswaService.getMahasiswaById(idMhs).orElse(null));

        model.addAttribute(
            "riwayatList",
            dosenService.getRiwayatBimbinganMahasiswa(idMhs)
        );

        model.addAttribute(
            "topikTA",
            mahasiswaService.getTopikTA(idMhs).getOrDefault("TopikTA", "-")
        );

        return "Dosen/detail-mhs";
    }


    @GetMapping("/jadwal-bimbingan")
    public String jadwalBimbinganDosen(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        // Notifikasi
        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));

        // (Nanti) data jadwal akan dimasukkan di sini
        // model.addAttribute("jadwalList", ...);

        return "Dosen/jadwal-bimbingan";
    }
    
    @GetMapping("/jadwal-mengajar")
    public String jadwalMengajar(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));
        model.addAttribute("jadwalMengajarList", dosenService.getJadwalMengajar(idDosen));

        return "Dosen/JadwalMengajar";
    }
}
