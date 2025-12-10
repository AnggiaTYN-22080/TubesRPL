package com.example.tubes.Auth;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    // Menampilkan Halaman Login
    @GetMapping("/login")
    public String loginPage() {
        return "Auth/login";
    }

    // Memproses Login
    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User user = authService.authenticate(email, password);

        if (user != null) {
            // Simpan User di Session
            session.setAttribute("currentUser", user);

            // Role yang di gunakan!! (Sesuaikan dengan data di database: 'admin', 'dosen',
            // 'mahasiswa')
            switch (user.getRole().toLowerCase()) {
                case "admin":
                    return "redirect:/admin/dashboard"; // Pastikan Anda punya AdminController
                case "dosen":
                    return "redirect:/dosen/dashboard";
                case "mahasiswa":
                    return "redirect:/mahasiswa/dashboard";
                default:
                    return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Email atau Password Salah!");
            return "Auth/login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // Redirect Root ke Login
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }
}