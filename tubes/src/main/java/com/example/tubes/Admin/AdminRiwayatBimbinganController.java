package com.example.tubes.Admin;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminRiwayatBimbinganController {

    @Autowired
    private AdminBimbinganService service;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return user != null
                && user.getRole() != null
                && user.getRole().equalsIgnoreCase("admin");
    }

    @GetMapping("/riwayat-bimbingan")
    public String riwayatPage(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        model.addAttribute("riwayatList", service.getRiwayat());

        return "Admin/riwayat-bimbingan";
    }

    @GetMapping("/detail-bimbingan/{idJadwal}")
    public String detailPage(@PathVariable int idJadwal, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var detailOpt = service.getDetail(idJadwal);
        if (detailOpt.isEmpty()) {
            return "redirect:/admin/riwayat-bimbingan";
        }

        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        model.addAttribute("detail", detailOpt.get());

        return "Admin/detail-bimbingan";
    }
}
