package com.example.tubes.Periode;

import com.example.tubes.Auth.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/periode-ta")
public class PeriodeAdminController {

    private final PeriodeService service;

    public PeriodeAdminController(PeriodeService service) {
        this.service = service;
    }

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("currentUser");
        return u != null && "admin".equalsIgnoreCase(u.getRole());
    }

    @GetMapping
    public String ta1(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("daftarPeriode", service.getTA1());
        model.addAttribute("periodeForm", new PeriodeForm());

        // PENTING: sesuaikan dengan nama file template kamu
        // kalau file kamu namanya: templates/Admin/periode-TA.html -> pakai "Admin/periode-TA"
        return "Admin/periode-TA";
    }

    @GetMapping("/ta2")
    public String ta2(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("daftarPeriode", service.getTA2());
        model.addAttribute("periodeForm", new PeriodeForm());

        return "Admin/periode-TA2";
    }

    @PostMapping("/simpan")
    public String simpan(@ModelAttribute("periodeForm") PeriodeForm form,
                         @RequestParam(value = "tab", required = false, defaultValue = "ta1") String tab,
                         HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        int fallbackSemester = "ta2".equalsIgnoreCase(tab) ? 2 : 1;
        service.simpan(form, fallbackSemester);

        return "redirect:/admin/periode-ta" + ("ta2".equalsIgnoreCase(tab) ? "/ta2" : "");
    }

    @PostMapping("/hapus/{id}")
    public String hapus(@PathVariable int id,
                        @RequestParam(value = "tab", required = false, defaultValue = "ta1") String tab,
                        HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        service.hapus(id);
        return "redirect:/admin/periode-ta" + ("ta2".equalsIgnoreCase(tab) ? "/ta2" : "");
    }
}
