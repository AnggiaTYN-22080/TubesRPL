package com.example.tubes.Admin;

import com.example.tubes.Auth.User;
import com.example.tubes.JadwalBimbingin.JadwalBimbinganService;
import com.example.tubes.JadwalBimbingin.JadwalRuanganView;
import com.example.tubes.Ruangan.Ruangan;
import com.example.tubes.Ruangan.RuanganService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class KelolaJadwalRuanganAdminController {

    @Autowired
    private JadwalBimbinganService jadwalService;

    @Autowired
    private RuanganService ruanganService;

    // =========================
    // PAGE (THYMELEAF)
    // =========================
    @GetMapping("/kelola-jadwal")
    public String kelolaJadwalPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        System.out.println("DEBUG /admin/kelola-jadwal currentUser=" + user);

        List<JadwalRuanganView> jadwalList = jadwalService.getAllForAdmin();
        model.addAttribute("user", user);
        model.addAttribute("jadwalList", jadwalList);

        // HTML kamu saat ini hanya pakai jadwalList, tapi list ruangan berguna
        // kalau nanti kamu tambah dropdown/popup ruangan.
        model.addAttribute("ruanganList", ruanganService.getAll());

        return "Admin/kelola-jadwal-&-ruangan";
    }

    // =========================
    // REST API (OPSIONAL)
    // =========================

    // ---------- RUANGAN CRUD ----------
    @GetMapping("/api/ruangan")
    @ResponseBody
    public ResponseEntity<?> getAllRuangan(HttpSession session) {
        if (!isAdmin(session))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        return ResponseEntity.ok(ruanganService.getAll());
    }

    @GetMapping("/api/ruangan/{id}")
    @ResponseBody
    public ResponseEntity<?> getRuanganById(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        Optional<Ruangan> r = ruanganService.getById(id);
        return r.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruangan not found"));
    }

    @PostMapping("/api/ruangan")
    @ResponseBody
    public ResponseEntity<?> createRuangan(@RequestBody Ruangan payload, HttpSession session) {
        if (!isAdmin(session))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        // wajib sesuai DB: namaRuangan, statusRuangan
        if (payload.getNamaRuangan() == null || payload.getNamaRuangan().isBlank()
                || payload.getStatusRuangan() == null || payload.getStatusRuangan().isBlank()) {
            return ResponseEntity.badRequest().body("namaRuangan dan statusRuangan wajib diisi");
        }

        int newId = ruanganService.create(payload.getNamaRuangan(), payload.getStatusRuangan());
        return ResponseEntity.status(HttpStatus.CREATED).body(newId);
    }

    @PutMapping("/api/ruangan/{id}")
    @ResponseBody
    public ResponseEntity<?> updateRuangan(@PathVariable int id, @RequestBody Ruangan payload, HttpSession session) {
        if (!isAdmin(session))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        if (payload.getNamaRuangan() == null || payload.getNamaRuangan().isBlank()
                || payload.getStatusRuangan() == null || payload.getStatusRuangan().isBlank()) {
            return ResponseEntity.badRequest().body("namaRuangan dan statusRuangan wajib diisi");
        }

        boolean ok = ruanganService.update(id, payload.getNamaRuangan(), payload.getStatusRuangan());
        if (!ok)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruangan not found");

        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/api/ruangan/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteRuangan(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        boolean ok = ruanganService.delete(id);
        if (!ok)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruangan not found");

        return ResponseEntity.ok("OK");
    }

    // ---------- JADWAL ADMIN ACTION ----------
    @PatchMapping("/api/jadwal/{id}/status")
    @ResponseBody
    public ResponseEntity<?> updateStatusJadwal(@PathVariable int id,
            @RequestParam("status") String status,
            HttpSession session) {
        if (!isAdmin(session))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().body("status wajib diisi");
        }

        boolean ok = jadwalService.updateStatusIfExists(id, status);
        if (!ok)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jadwal not found");

        return ResponseEntity.ok("OK");
    }

    @PatchMapping("/api/jadwal/{id}/ruangan")
    @ResponseBody
    public ResponseEntity<?> setRuanganJadwal(@PathVariable int id,
            @RequestParam(value = "idRuangan", required = false) Integer idRuangan,
            HttpSession session) {
        if (!isAdmin(session))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        // idRuangan boleh null untuk mengosongkan ruangan
        if (idRuangan != null && ruanganService.getById(idRuangan).isEmpty()) {
            return ResponseEntity.badRequest().body("idRuangan tidak valid");
        }

        boolean ok = jadwalService.updateRuanganIfExists(id, idRuangan);
        if (!ok)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jadwal not found");

        return ResponseEntity.ok("OK");
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return user != null && user.getRole() != null && user.getRole().equalsIgnoreCase("admin");
    }

}
