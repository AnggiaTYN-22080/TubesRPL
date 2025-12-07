package com.example.tubes.Mahasiswa;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        // Memanggil melewati service
        Mahasiswa mhsDetail = mahasiswaService.getMahasiswaByUserId(user.getId()).orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("mhsDetail", mhsDetail);

        return "Mahasiswa/dashboard-mhs";
    }
}