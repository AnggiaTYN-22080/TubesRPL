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
        // Mengambil user dari session
        User user = (User) session.getAttribute("currentUser");

        // Melakukan validasi role
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        // Mengambil detail dosen dari Database
        Dosen dosenDetail = dosenService.getDosenByUserId(user.getId()).orElse(null);

        // Mengirim data ke html
        model.addAttribute("user", user);
        model.addAttribute("dosenDetail", dosenDetail);

        return "Dosen/dashboard";
    }

}