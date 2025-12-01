package com.example.tubes.Mahasiswa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.tubes.Auth.User;
import com.example.tubes.Jadwal.Jadwal;

import jakarta.servlet.http.HttpSession;

public class MahasiswaController {
    @Autowired
    private JdbcMahasiswaRepo mahasiswaRepository; // Pastikan nama variabel sesuai

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"MAHASISWA".equals(user.getRole())) {
            return "redirect:/login";
        }

        // 1. Ambil Jadwal Mahasiswa ini dari Database
        List<Jadwal> jadwalSaya = mahasiswaRepository.findJadwalByMahasiswaId(user.getId());

        // 2. Kirim ke HTML
        model.addAttribute("user", user);
        model.addAttribute("jadwalList", jadwalSaya);

        return "Mahasiswa/dashboard";
    }
}
