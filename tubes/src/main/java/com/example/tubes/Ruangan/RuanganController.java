package com.example.tubes.Ruangan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/api/ruangan")
public class RuanganController {

    @Autowired
    private RuanganService ruanganService;

    // Endpoint ini akan dipanggil oleh JavaScript (fetch)
    @GetMapping("/tersedia")
    @ResponseBody // Penting: Agar return value dianggap JSON, bukan nama file HTML
    public List<Ruangan> getRuanganTersedia(
            @RequestParam("tanggal") LocalDate tanggal,
            @RequestParam("jamMulai") LocalTime jamMulai,
            @RequestParam("jamSelesai") LocalTime jamSelesai) {
        return ruanganService.cariRuanganKosong(tanggal, jamMulai, jamSelesai);
    }
}