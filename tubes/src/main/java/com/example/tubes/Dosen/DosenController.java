package com.example.tubes.Dosen;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dosen")
public class DosenController {

    @Autowired
    private DosenService dosenService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");

        // Cek Login & Role
        if (user == null || !"DOSEN".equals(user.getRole())) {
            return "redirect:/login";
        }

        Dosen dataLengkapDosen = dosenService.getDosenByNik(user.getIdUser()).orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("dosenDetail", dataLengkapDosen);

        return "Dosen/dashboard";
    }

    @GetMapping("/mahasiswa")
    public String listMahasiswa() {
        return "Dosen/daftar-mhs";
    }

    @GetMapping("/mahasiswa/detail/1")
    public String detailMahasiswa() {
        return "Dosen/detail-mahasiswa";
    }

    @GetMapping("/bimbingan/detail/1")
    public String detailBimbingan() {
        return "Dosen/detail-bimbingan";
    }
}