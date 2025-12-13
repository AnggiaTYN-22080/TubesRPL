package com.example.tubes.Dosen;
import com.example.tubes.Auth.User;
import com.example.tubes.Bimbingan.BimbinganService;
import com.example.tubes.JadwalBimbingin.JadwalBimbingan;
import com.example.tubes.JadwalBimbingin.JadwalBimbinganService;
import com.example.tubes.Mahasiswa.MahasiswaService;
import com.example.tubes.Notifikasi.Notifikasi;
import com.example.tubes.Notifikasi.NotifikasiService;
import com.example.tubes.Ruangan.Ruangan;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/dosen")
public class DosenController {

    @Autowired
    private DosenService dosenService;

    @Autowired
    private NotifikasiService notifService;

    @Autowired
    private MahasiswaService mahasiswaService;

    @Autowired
    private BimbinganService bimbinganService;

    @Autowired
    private JadwalBimbinganService jadwalService;


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        List<Notifikasi> notifList = notifService.getNotifByUser(idDosen);
        model.addAttribute("notifList", notifList);

        int jumlahMahasiswa = dosenService.getJumlahMahasiswa(idDosen);
        model.addAttribute("jumlahMahasiswa", jumlahMahasiswa);

        int totalPengajuan = dosenService.getTotalPengajuan(idDosen);
        model.addAttribute("totalPengajuan", totalPengajuan);

        model.addAttribute("user", user);
        model.addAttribute("dosenDetail", dosenService.getDosenByUserId(idDosen).orElse(null));

        return "Dosen/DashboardDosen";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/daftar-mahasiswa")
    public String daftarMahasiswa(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();
        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));

        model.addAttribute("mahasiswaList", dosenService.getMahasiswaBimbingan(idDosen));

        return "Dosen/daftar-mhs";
    }

    @GetMapping("/mahasiswa/detail/{idMhs}")
    public String detailMahasiswa(
            @PathVariable int idMhs,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));
        model.addAttribute("mhs", mahasiswaService.getMahasiswaById(idMhs).orElse(null));

        model.addAttribute(
            "riwayatList",
            dosenService.getRiwayatBimbinganMahasiswa(idMhs)
        );

        model.addAttribute(
            "topikTA",
            mahasiswaService.getTopikTA(idMhs).getOrDefault("TopikTA", "-")
        );

        return "Dosen/detail-mhs";
    }


    @GetMapping("/jadwal-bimbingan")
    public String jadwalBimbinganDosen(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            HttpSession session, 
            Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        // Notifikasi
        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));

        // Get current date if not specified
        java.time.LocalDate now = java.time.LocalDate.now();
        int currentYear = (year != null) ? year : now.getYear();
        int currentMonth = (month != null) ? month : now.getMonthValue();

        // Get jadwal bimbingan from database
        List<JadwalBimbingan> jadwalListRaw = jadwalService.getByMonth(idDosen, currentYear, currentMonth);
        
        // Filter and format data for template (with null safety)
        List<Map<String, Object>> jadwalListFormatted = new java.util.ArrayList<>();
        if (jadwalListRaw != null) {
            for (JadwalBimbingan j : jadwalListRaw) {
                if (j != null && j.getTanggal() != null && j.getWaktuMulai() != null && j.getWaktuSelesai() != null) {
                    Map<String, Object> jadwalMap = new java.util.HashMap<>();
                    jadwalMap.put("idJadwal", j.getIdJadwal());
                    jadwalMap.put("day", j.getTanggal().getDayOfMonth());
                    jadwalMap.put("month", j.getTanggal().getMonthValue() - 1); // JavaScript uses 0-based months
                    jadwalMap.put("year", j.getTanggal().getYear());
                    jadwalMap.put("nama", (j.getNamaMahasiswa() != null && !j.getNamaMahasiswa().isEmpty()) ? j.getNamaMahasiswa() : "Mahasiswa");
                    jadwalMap.put("topik", (j.getTopikTA() != null && !j.getTopikTA().isEmpty()) ? j.getTopikTA() : "-");
                    jadwalMap.put("waktuMulai", j.getWaktuMulai().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                    jadwalMap.put("waktuSelesai", j.getWaktuSelesai().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                    jadwalMap.put("ruangan", (j.getNamaRuangan() != null && !j.getNamaRuangan().isEmpty()) ? j.getNamaRuangan(): "-");
                    jadwalListFormatted.add(jadwalMap);
                }
            }
        }
        
        model.addAttribute("jadwalList", jadwalListFormatted);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("currentMonthJS", currentMonth - 1); 

        return "Dosen/jadwal-bimbingan";
    }
    
    @GetMapping("/api/jadwal-bimbingan")
    @ResponseBody
    public List<Map<String, Object>> getJadwalBimbinganAPI(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            HttpSession session) {
        
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return new java.util.ArrayList<>();
        }
        
        int idDosen = user.getId();
    
        java.time.LocalDate now = java.time.LocalDate.now();
        int currentYear = (year != null) ? year : now.getYear();
        int currentMonth = (month != null) ? month : now.getMonthValue();
        
        List<JadwalBimbingan> jadwalListRaw = jadwalService.getByMonth(idDosen, currentYear, currentMonth);
        
        List<Map<String, Object>> jadwalListFormatted = new java.util.ArrayList<>();
        if (jadwalListRaw != null) {
            for (JadwalBimbingan j : jadwalListRaw) {
                if (j != null && j.getTanggal() != null && j.getWaktuMulai() != null && j.getWaktuSelesai() != null) {
                    Map<String, Object> jadwalMap = new java.util.HashMap<>();
                    jadwalMap.put("idJadwal", j.getIdJadwal());
                    jadwalMap.put("day", j.getTanggal().getDayOfMonth());
                    jadwalMap.put("month", j.getTanggal().getMonthValue() - 1);
                    jadwalMap.put("year", j.getTanggal().getYear());
                    jadwalMap.put("nama", (j.getNamaMahasiswa() != null && !j.getNamaMahasiswa().isEmpty()) ? j.getNamaMahasiswa() : "Mahasiswa");
                    jadwalMap.put("topik", (j.getTopikTA() != null && !j.getTopikTA().isEmpty()) ? j.getTopikTA() : "-");
                    jadwalMap.put("waktuMulai", j.getWaktuMulai().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                    jadwalMap.put("waktuSelesai", j.getWaktuSelesai().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                    jadwalMap.put("ruangan", (j.getNamaRuangan() != null && !j.getNamaRuangan().isEmpty()) ? j.getNamaRuangan(): "-");
                    jadwalListFormatted.add(jadwalMap);
                }
            }
        }
        
        return jadwalListFormatted;
    }
    
    @GetMapping("/jadwal-mengajar")
    public String jadwalMengajar(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));
        model.addAttribute("jadwalMengajarList", dosenService.getJadwalMengajar(idDosen));

        return "Dosen/JadwalMengajar";
    }

    @GetMapping("/bimbingan/{id}")
    public String detailBimbingan(
            @PathVariable int id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("notifList", notifService.getNotifByUser(user.getId()));

        model.addAttribute("jadwal", jadwalService.getById(id).orElse(null));

        model.addAttribute("bimbingan", bimbinganService.getByJadwal(id).orElse(null));

        return "Dosen/detail-bimbingan";
    }

    @PostMapping("/bimbingan/{id}/catatan")
    public String simpanCatatan(
            @PathVariable int id,
            @RequestParam String catatan,
            HttpSession session) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        bimbinganService.simpanCatatan(id, catatan);

        return "redirect:/dosen/bimbingan/" + id;
    }

    @GetMapping("/jadwal-mengajar/{idJadwalKuliah}")
    public String getJadwalMengajarById(
            @PathVariable int idJadwalKuliah,
            HttpSession session,
            Model model) {
        
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();
        Optional<Map<String, Object>> jadwal = dosenService.getJadwalMengajarById(idDosen, idJadwalKuliah);
        
        if (jadwal.isPresent()) {
            model.addAttribute("jadwal", jadwal.get());
            return "redirect:/dosen/jadwal-mengajar";
        }
        
        return "redirect:/dosen/jadwal-mengajar?error=notfound";
    }

    @PostMapping("/jadwal-mengajar/tambah")
    public String tambahJadwalMengajar(
            @RequestParam String hari,
            @RequestParam String jamMulai,
            @RequestParam String jamSelesai,
            @RequestParam String mataKuliah,
            @RequestParam String kelas,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();
        
        try {
            dosenService.tambahJadwalMengajar(idDosen, hari, jamMulai, jamSelesai, mataKuliah, kelas);
            redirectAttributes.addFlashAttribute("success", "Jadwal berhasil ditambahkan");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menambahkan jadwal: " + e.getMessage());
        }
        
        return "redirect:/dosen/jadwal-mengajar";
    }

    @PostMapping("/jadwal-mengajar/ubah/{idJadwalKuliah}")
    public String ubahJadwalMengajar(
            @PathVariable int idJadwalKuliah,
            @RequestParam String hari,
            @RequestParam String jamMulai,
            @RequestParam String jamSelesai,
            @RequestParam String mataKuliah,
            @RequestParam String kelas,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();
        
        try {
            dosenService.ubahJadwalMengajar(idDosen, idJadwalKuliah, hari, jamMulai, jamSelesai, mataKuliah, kelas);
            redirectAttributes.addFlashAttribute("success", "Jadwal berhasil diubah");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengubah jadwal: " + e.getMessage());
        }
        
        return "redirect:/dosen/jadwal-mengajar";
    }

    @PostMapping("/jadwal-mengajar/hapus/{idJadwalKuliah}")
    public String hapusJadwalMengajar(
            @PathVariable int idJadwalKuliah,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();
        
        try {
            dosenService.hapusJadwalMengajar(idDosen, idJadwalKuliah);
            redirectAttributes.addFlashAttribute("success", "Jadwal berhasil dihapus");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus jadwal: " + e.getMessage());
        }
        
        return "redirect:/dosen/jadwal-mengajar";
    }

    @PostMapping("/jadwal-mengajar/import-csv")
    public String importJadwalMengajarFromCSV(
            @RequestParam("file") MultipartFile file,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "File CSV tidak boleh kosong");
            return "redirect:/dosen/jadwal-mengajar";
        }

        int idDosen = user.getId();
        
        try {
            List<Map<String, String>> csvData = parseCSV(file);
            int[] results = dosenService.importJadwalMengajarFromCSV(idDosen, csvData);
            
            redirectAttributes.addFlashAttribute("success", 
                String.format("Import berhasil: %d jadwal ditambahkan, %d gagal", results[0], results[1]));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengimport CSV: " + e.getMessage());
        }
        
        return "redirect:/dosen/jadwal-mengajar";
    }

    private List<Map<String, String>> parseCSV(MultipartFile file) throws Exception {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            String[] headers = null;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                if (line.isEmpty()) continue;
                
                // Simple CSV parsing (split by comma, handle quotes if needed)
                List<String> values = parseCSVLine(line);
                
                if (lineNumber == 1) {
                    // Header row
                    headers = values.toArray(new String[0]);
                    for (int i = 0; i < headers.length; i++) {
                        headers[i] = headers[i].trim().toLowerCase();
                    }
                } else {
                    // Data row
                    if (values.size() == headers.length) {
                        Map<String, String> row = new HashMap<>();
                        for (int i = 0; i < headers.length; i++) {
                            row.put(headers[i], values.get(i).trim());
                        }
                        data.add(row);
                    }
                }
            }
        }
        
        return data;
    }
    
    private List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result;
    }

    
    @GetMapping("/ajukan-bimbingan")
    public String ajukanBimbinganPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        model.addAttribute("notifList", notifService.getNotifByUser(idDosen));
        model.addAttribute("mahasiswaList", dosenService.getMahasiswaBimbingan(idDosen));

        return "Dosen/ajukan-bimbingan";
    }

    @GetMapping("/ruangan-available")
    @ResponseBody
    public List<Ruangan> ruanganAvailable(
            HttpSession session,
            @RequestParam String tanggal,
            @RequestParam String mulai,
            @RequestParam String selesai
    ) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return List.of();
        }

        LocalDate tgl = LocalDate.parse(tanggal);
        LocalTime m = LocalTime.parse(mulai);
        LocalTime s = LocalTime.parse(selesai);

        return jadwalService.getAvailableRuangan(tgl, m, s);
    }

    @PostMapping("/ajukan-bimbingan")
    public String submitAjukanBimbingan(
            HttpSession session,
            @RequestParam int idMhs,
            @RequestParam String tanggal,
            @RequestParam String jamMulai,
            @RequestParam String jamSelesai,
            @RequestParam int idRuangan,
            RedirectAttributes ra
    ) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        int idDosen = user.getId();

        LocalDate tgl = LocalDate.parse(tanggal);
        LocalTime mulai = LocalTime.parse(jamMulai);
        LocalTime selesai = LocalTime.parse(jamSelesai);

        // Insert jadwal bimbingan dari dosen -> biasanya langsung approved
        // (kalau mau jadi pending juga bisa, tinggal ganti status)
        jadwalService.insertPengajuanDosen(idMhs, idDosen, idRuangan, tgl, mulai, selesai, "approved");

        // Notif ke mahasiswa
        notifService.buatNotif(
                idMhs,
                "Jadwal Bimbingan",
                "Dosen mengajukan jadwal bimbingan pada " + tgl + " (" + mulai + " - " + selesai + ")."
        );

        // Flash message ke dosen
        ra.addFlashAttribute("successMessage", "Jadwal bimbingan berhasil diajukan & notifikasi terkirim ke mahasiswa.");

        return "redirect:/dosen/ajukan-bimbingan";
    }

}
