package com.example.tubes.Periode;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PeriodeService {

    private final JdbcPeriodeRepository repo;

    public PeriodeService(JdbcPeriodeRepository repo) {
        this.repo = repo;
    }

    public List<PeriodeView> getTA1() {
        return repo.findBySemester(1).stream()
                .map(this::toView)
                .toList();
    }

    public List<PeriodeView> getTA2() {
        return repo.findBySemester(2).stream()
                .map(this::toView)
                .toList();
    }

    public void simpan(PeriodeForm form, int fallbackSemester) {
        // Karena DB tidak punya kolom "nama", kita pakai input nama hanya untuk menentukan TA1/TA2 dan TahunAjaran
        String nama = (form.getNama() == null) ? "" : form.getNama().trim();

        int semester = detectSemesterFromNama(nama, fallbackSemester);
        String tahunAjaran = detectTahunAjaran(nama);

        LocalDate mulai = parseDateFlexible(form.getTanggalMulai());
        LocalDate selesai = parseDateFlexible(form.getTanggalSelesai());

        repo.insert(tahunAjaran, semester, mulai, selesai);
    }

    public void hapus(int idPeriodeTA) {
        repo.deleteById(idPeriodeTA);
    }

    private PeriodeView toView(PeriodeTA p) {
        String ganjilGenap = (p.getSemester() == 1) ? "Ganjil" : "Genap";
        String nama = "TA" + p.getSemester() + " " + ganjilGenap + " " + p.getTahunAjaran();
        return new PeriodeView(p.getIdPeriodeTA(), nama, p.getTanggalMulaiPeriodeTA(), p.getTanggalSelesaiPeriodeTA());
    }

    private int detectSemesterFromNama(String nama, int fallback) {
        String n = nama.toLowerCase();
        if (n.contains("ta2")) return 2;
        if (n.contains("ta 2")) return 2;
        if (n.contains("ta1")) return 1;
        if (n.contains("ta 1")) return 1;
        return fallback;
    }

    private String detectTahunAjaran(String nama) {
        // kalau user input mengandung pola "2025/2026" pakai itu, kalau tidak fallback "2025/2026"
        // (silakan kamu sesuaikan kalau punya aturan lain)
        if (nama != null && nama.matches(".*\\d{4}/\\d{4}.*")) {
            return nama.replaceAll(".*(\\d{4}/\\d{4}).*", "$1");
        }
        return "2025/2026";
    }

    private LocalDate parseDateFlexible(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String s = raw.trim();

        // 1) yyyy-MM-dd (paling aman)
        try { return LocalDate.parse(s); } catch (Exception ignored) {}

        // 2) MM/dd/yy
        try { return LocalDate.parse(s, DateTimeFormatter.ofPattern("MM/dd/yy")); } catch (Exception ignored) {}

        // 3) MM/dd/yyyy
        try { return LocalDate.parse(s, DateTimeFormatter.ofPattern("MM/dd/yyyy")); } catch (Exception ignored) {}

        throw new IllegalArgumentException("Format tanggal tidak dikenali: " + raw);
    }
}
