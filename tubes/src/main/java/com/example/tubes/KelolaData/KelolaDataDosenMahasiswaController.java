package com.example.tubes.KelolaData;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KelolaDataDosenMahasiswaController {

    private final KelolaDataService service;

    public KelolaDataDosenMahasiswaController(KelolaDataService service) {
        this.service = service;
    }

    @GetMapping("/admin/dosen")
    public String pageDosen(Model model) {
        model.addAttribute("listDosen", service.getAllDosen());
        model.addAttribute("listPeriode", service.getAllPeriode());
        return "Admin/data-dosen";
    }

    @GetMapping("/admin/mahasiswa")
    public String pageMahasiswa(Model model) {
        model.addAttribute("listMahasiswa", service.getAllMahasiswa());
        model.addAttribute("listPeriode", service.getAllPeriode());
        model.addAttribute("listDosen", service.getAllDosen());
        return "Admin/data-mahasiswa";
    }

}
