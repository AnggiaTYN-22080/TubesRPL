package com.example.tubes.KelolaData;

import com.example.tubes.KelolaData.dto.TopikSaveAllForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TopikTAController {

    private final KelolaDataService service;

    public TopikTAController(KelolaDataService service) {
        this.service = service;
    }

    // TA1 (semester 1)
    @GetMapping("/admin/kelola-data/list")
    public String topikTA1(@RequestParam(value = "periodeId", required = false) Integer periodeId,
                           Model model) {

        model.addAttribute("periodeList", service.getPeriodeSemester(1));
        model.addAttribute("selectedPeriodeId", periodeId);

        model.addAttribute("mahasiswaList",
                (periodeId == null) ? java.util.List.of() : service.getTopikRows(periodeId));

        return "Admin/setting-topik-ta";
    }

    // TA2 (semester 2)
    @GetMapping("/admin/kelola-data/list/ta2")
    public String topikTA2(@RequestParam(value = "periodeId", required = false) Integer periodeId,
                           Model model) {

        model.addAttribute("periodeList", service.getPeriodeSemester(2));
        model.addAttribute("selectedPeriodeId", periodeId);

        model.addAttribute("mahasiswaList",
                (periodeId == null) ? java.util.List.of() : service.getTopikRows(periodeId));

        return "Admin/setting-topik-ta2";
    }

    // sesuai HTML: th:action="@{/admin/topik-ta/simpan-semua}" :contentReference[oaicite:7]{index=7}
    @PostMapping("/admin/topik-ta/simpan-semua")
    public String simpanSemua(@ModelAttribute TopikSaveAllForm form) {
        service.simpanSemuaTopik(form);

        Integer periodeId = form.getPeriodeId();
        if (periodeId == null) return "redirect:/admin/kelola-data/list";

        // balik ke page yang lagi dibuka: biasanya cukup redirect dengan periodeId
        return "redirect:/admin/kelola-data/list?periodeId=" + periodeId;
    }

    // sesuai HTML: th:action="@{/admin/topik-ta/reset}" :contentReference[oaicite:8]{index=8}
    @PostMapping("/admin/topik-ta/reset")
    public String reset(@RequestParam("mahasiswaId") int mahasiswaId,
                        @RequestParam("periodeId") int periodeId) {

        service.resetTopik(mahasiswaId, periodeId);
        return "redirect:/admin/kelola-data/list?periodeId=" + periodeId;
    }
}
