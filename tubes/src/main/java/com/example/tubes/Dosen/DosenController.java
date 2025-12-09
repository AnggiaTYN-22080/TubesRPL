package com.example.tubes.Dosen;
import com.example.tubes.Auth.User;
import com.example.tubes.Notifikasi.Notifikasi;
import com.example.tubes.Notifikasi.NotifikasiService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dosen")
public class DosenController {

    @Autowired
    private DosenService dosenService;

    @Autowired
    private NotifikasiService notifService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        // Ambil notifikasi
        List<Notifikasi> notifList = notifService.getNotifByUser(idDosen);
        model.addAttribute("notifList", notifList);

        // Ambil jumlah mahasiswa bimbingan
        int jumlahMahasiswa = dosenService.getJumlahMahasiswa(idDosen);
        model.addAttribute("jumlahMahasiswa", jumlahMahasiswa);

        // Ambil jumlah pengajuan pending
        int totalPengajuan = dosenService.getTotalPengajuan(idDosen);
        model.addAttribute("totalPengajuan", totalPengajuan);

        // Detail user dosen
        model.addAttribute("user", user);
        model.addAttribute("dosenDetail", dosenService.getDosenByUserId(idDosen).orElse(null));

        return "Dosen/DashboardDosen";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}