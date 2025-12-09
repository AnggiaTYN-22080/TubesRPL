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

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");

        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // Ambil data detail mahasiswa (termasuk NPM)
        Mahasiswa mhsDetail = mahasiswaService.getMahasiswaByUserId(user.getId()).orElse(null);

        if (mhsDetail != null) {
            // Ambil data Topik TA
            Map<String, Object> topik = mahasiswaService.getTopikTA(mhsDetail.getIdMhs());

            // Ambil data Jadwal Berikutnya
            Map<String, Object> nextBimbingan = mahasiswaService.getNextBimbingan(mhsDetail.getIdMhs());

            model.addAttribute("mhsDetail", mhsDetail);
            model.addAttribute("topik", topik);
            model.addAttribute("nextBimbingan", nextBimbingan);
        }

        model.addAttribute("user", user);

        return "Mahasiswa/dashboard-mhs";
    }
}