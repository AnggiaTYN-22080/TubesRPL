package com.example.tubes.Admin;

import com.example.tubes.Auth.User;
import com.example.tubes.Periode.PeriodeTA;
import com.example.tubes.Periode.PeriodeTAService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//@Controller
@RequestMapping("/admin")
public class AdminPeriodeTAController {

    @Autowired
    private PeriodeTAService service;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }

    @GetMapping("/periode-ta")
    public String page(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("user", session.getAttribute("currentUser"));
        model.addAttribute("periodeList", service.getAll());
        model.addAttribute("periodeForm", new PeriodeTA());

        return "Admin/periode-ta";
    }

    @PostMapping("/periode-ta/tambah")
    public String tambah(@ModelAttribute PeriodeTA form, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        service.tambah(form);
        return "redirect:/admin/periode-ta";
    }

    @PostMapping("/periode-ta/update")
    public String update(@ModelAttribute PeriodeTA form, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        service.update(form);
        return "redirect:/admin/periode-TA";
    }

    @PostMapping("/periode-ta/hapus/{id}")
    public String hapus(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        service.hapus(id);
        return "redirect:/admin/periode-TA";
    }
}
