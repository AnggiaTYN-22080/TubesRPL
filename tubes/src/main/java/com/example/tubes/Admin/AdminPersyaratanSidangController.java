package com.example.tubes.Admin;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminPersyaratanSidangController {

    @Autowired
    private PersyaratanSidangService service;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return user != null && user.getRole() != null && user.getRole().equalsIgnoreCase("admin");
    }

    @GetMapping("/persyaratan-sidang")
    public String ta1(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("daftarPersyaratan", service.getTA1());
        return "Admin/persyaratan-sidang";
    }

    @GetMapping("/persyaratan-sidang/ta2")
    public String ta2(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("daftarPersyaratan", service.getTA2());
        return "Admin/persyaratan-sidang2";
    }
}
