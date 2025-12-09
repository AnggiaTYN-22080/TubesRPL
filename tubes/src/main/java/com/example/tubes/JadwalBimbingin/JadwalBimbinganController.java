package com.example.tubes.JadwalBimbingin;

import com.example.tubes.Auth.User;
import com.example.tubes.Notifikasi.NotifikasiService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dosen")
public class JadwalBimbinganController {

    @Autowired
    private JadwalBimbinganService service;

    @Autowired
    private NotifikasiService notifService;

    @GetMapping("/pengajuan")
    public String pengajuan(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        // Data pengajuan
        List<JadwalBimbingan> pengajuanList = service.getPengajuanByDosen(idDosen);
        model.addAttribute("pengajuanList", pengajuanList);

        // Notifikasi
        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));

        return "Dosen/pengajuan-bimbingan";
    }

    @PostMapping("/pengajuan/{id}/approve")
    public String approve(@PathVariable int id) {

        // Ubah status di jadwal_bimbingan
        service.setStatus(id, "approved");

        // Ambil data jadwal untuk cari idMhs
        service.getById(id).ifPresent(j -> {
            int idMhs = j.getIdMhs();
            notifService.buatNotif(idMhs, "Pengajuan Bimbingan", "Pengajuan bimbingan Anda telah DISETUJUI dosen pembimbing."
            );
        });

        return "redirect:/dosen/pengajuan";
    }

    @PostMapping("/pengajuan/{id}/reject")
    public String reject(@PathVariable int id) {

        // Ubah status di jadwal_bimbingan
        service.setStatus(id, "rejected");

        // Ambil data jadwal untuk cari idMhs
        service.getById(id).ifPresent(j -> {
            int idMhs = j.getIdMhs();
            notifService.buatNotif(idMhs, "Pengajuan Bimbingan", "Pengajuan bimbingan Anda DITOLAK. Silakan ajukan jadwal lain."
            );
        });

        return "redirect:/dosen/pengajuan";
    }
}