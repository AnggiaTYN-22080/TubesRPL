package com.example.tubes.Mahasiswa;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

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
        return "Mahasiswa/dashboard-mhs";
    }

    @GetMapping("/jadwal")
    public String jadwal(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        // Nanti bisa tambah logic ambil data jadwal disini
        return "Mahasiswa/jadwal-mhs";
    }

    @GetMapping("/riwayat")
    public String riwayat(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        // Nanti bisa tambah logic ambil data riwayat disini
        return "Mahasiswa/riwayat-bimbingan";
    }

    @GetMapping("/pengajuan")
    public String pengajuan(HttpSession session, Model model) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        return "Mahasiswa/pengajuan-mhs";
    }
}