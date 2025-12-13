// package com.example.tubes.KelolaData;

// import com.example.tubes.Auth.User;
// import jakarta.servlet.http.HttpSession;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// @Controller
// @RequestMapping("/admin")
// public class AdminKelolaDataController {

//     @Autowired
//     private KelolaDataService service;

//     // samain pola kamu sebelumnya (biar aman testing lokal)
//     private void ensureTestingAdminSession(HttpSession session) {
//         User user = (User) session.getAttribute("currentUser");
//         if (user == null) {
//             User dummy = new User(0, "Testing Admin", "test@local", "", "admin");
//             session.setAttribute("currentUser", dummy);
//         }
//     }

//     @GetMapping("/kelola-data")
//     public String page(HttpSession session, Model model) {
//         ensureTestingAdminSession(session);

//         model.addAttribute("user", session.getAttribute("currentUser"));

//         // data tabel
//         model.addAttribute("mahasiswaList", service.getMahasiswa());
//         model.addAttribute("dosenList", service.getDosen());
//         model.addAttribute("topikList", service.getTopik());

//         // form
//         model.addAttribute("mahasiswaForm", new MahasiswaForm());
//         model.addAttribute("dosenForm", new DosenForm());
//         model.addAttribute("topikForm", new TopikTAForm());

//         return "Admin/kelola-data"; // pastikan file HTML-nya ini ya
//     }

//     // ==== CREATE ====
//     @PostMapping("/kelola-data/mahasiswa/tambah")
//     public String tambahMahasiswa(@ModelAttribute MahasiswaForm mahasiswaForm, HttpSession session) {
//         ensureTestingAdminSession(session);
//         service.tambahMahasiswa(mahasiswaForm);
//         return "redirect:/admin/kelola-data";
//     }

//     @PostMapping("/kelola-data/dosen/tambah")
//     public String tambahDosen(@ModelAttribute DosenForm dosenForm, HttpSession session) {
//         ensureTestingAdminSession(session);
//         service.tambahDosen(dosenForm);
//         return "redirect:/admin/kelola-data";
//     }

//     @PostMapping("/kelola-data/topik/tambah")
//     public String tambahTopik(@ModelAttribute TopikTAForm topikForm, HttpSession session) {
//         ensureTestingAdminSession(session);
//         service.tambahTopik(topikForm);
//         return "redirect:/admin/kelola-data";
//     }

//     // ==== DELETE ====
//     @PostMapping("/kelola-data/mahasiswa/{idUser}/hapus")
//     public String hapusMahasiswa(@PathVariable int idUser, HttpSession session) {
//         ensureTestingAdminSession(session);
//         service.hapusMahasiswa(idUser);
//         return "redirect:/admin/kelola-data";
//     }

//     @PostMapping("/kelola-data/dosen/{idUser}/hapus")
//     public String hapusDosen(@PathVariable int idUser, HttpSession session) {
//         ensureTestingAdminSession(session);
//         service.hapusDosen(idUser);
//         return "redirect:/admin/kelola-data";
//     }

//     @PostMapping("/kelola-data/topik/{idTA}/hapus")
//     public String hapusTopik(@PathVariable int idTA, HttpSession session) {
//         ensureTestingAdminSession(session);
//         service.hapusTopik(idTA);
//         return "redirect:/admin/kelola-data";
//     }
// }
