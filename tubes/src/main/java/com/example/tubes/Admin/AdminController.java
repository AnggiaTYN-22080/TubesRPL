package com.example.tubes.Admin;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminDashboardService dashboardService;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return user != null && user.getRole() != null && user.getRole().equalsIgnoreCase("admin");
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);

        model.addAttribute("totalMahasiswa", dashboardService.getTotalMahasiswa());
        model.addAttribute("totalDosenPembimbing", dashboardService.getTotalDosenPembimbing());
        model.addAttribute("pengajuanAktif", dashboardService.getPengajuanAktif());
        model.addAttribute("notifList", dashboardService.getNotifList(user.getId()));

        // ⚠️ PENTING: samakan dengan nama file html kamu:
        // - kalau file kamu: templates/Admin/dashboard.html -> return "Admin/dashboard"
        // - kalau file kamu: templates/Admin/dashboardAdmin.html -> return "Admin/dashboardAdmin"
        return "Admin/DashboardAdmin";
    }

    
}
