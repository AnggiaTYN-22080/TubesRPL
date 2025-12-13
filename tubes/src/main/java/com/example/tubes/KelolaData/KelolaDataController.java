package com.example.tubes.KelolaData;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KelolaDataController {

    @GetMapping("/admin/kelola-data")
    public String menu() {
        // templates/Admin/kelola-data-mhs-&-dosen.html
        return "Admin/kelola-data-mhs-&-dosen";
    }
}