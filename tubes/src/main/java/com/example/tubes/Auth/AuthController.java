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
        return "Auth/login"; // Benar: Mengarah ke templates/Auth/login.html
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

            // Redirect sesuai Role (Gunakan huruf KECIL agar sesuai standar URL)
            switch (user.getRole()) {
                case "ADMIN":
                    return "redirect:/admin/dashboard"; // Ubah jadi kecil
                case "DOSEN":
                    return "redirect:/dosen/dashboard"; // Ubah jadi kecil
                case "MAHASISWA":
                    return "redirect:/mahasiswa/dashboard"; // Ubah jadi kecil
                default:
                    return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Email atau Password Salah!");
            // PERBAIKAN PENTING DISINI:
            return "Auth/login"; // Harus pakai "Auth/" biar tidak error 404 saat salah password
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