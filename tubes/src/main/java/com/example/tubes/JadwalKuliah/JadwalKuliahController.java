package com.example.tubes.JadwalKuliah;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/jadwal-kuliah")
public class JadwalKuliahController {

    @Autowired
    private JadwalKuliahService jadwalKuliahService;

    // Helper cek login
    private User cekLogin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return null;
        }
        return user;
    }

    @PostMapping("/tambah")
    public String tambahJadwal(
            HttpSession session,
            @ModelAttribute JadwalKuliah jadwalKuliah) {
        User user = cekLogin(session);
        if (user == null)
            return "redirect:/login";

        // Simpan jadwal (Service akan mengurus insert ke 2 tabel)
        jadwalKuliahService.save(user.getId(), jadwalKuliah);

        // Redirect kembali ke halaman list jadwal
        return "redirect:/mahasiswa/jadwal";
    }

    @PostMapping("/hapus/{id}")
    public String hapusJadwal(HttpSession session, @PathVariable("id") int id) {
        User user = cekLogin(session);
        if (user == null) {
            return "redirect:/login";
        }

        // Panggil service untuk menghapus
        jadwalKuliahService.hapusJadwal(id);

        // Redirect kembali ke halaman list jadwal
        return "redirect:/mahasiswa/jadwal";
    }
}