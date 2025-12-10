package com.example.tubes.Admin;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Cek apakah user sudah login dan role-nya ADMIN
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        // Mengambil daftar semua admin dari database
        List<Admin> listAdmin = adminService.getAllAdmins();

        // 3. Masukkan data ke Model agar bisa dibaca di HTML
        model.addAttribute("user", user);
        model.addAttribute("admins", listAdmin);

        model.addAttribute("user", user);
        return "Admin/dashboard"; // Mengarah ke file html: resources/templates/admin/dashboard.html
    }
}