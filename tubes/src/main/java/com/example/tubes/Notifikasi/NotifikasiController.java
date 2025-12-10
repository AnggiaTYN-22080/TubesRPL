package com.example.tubes.Notifikasi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifikasi")
public class NotifikasiController {

    @Autowired
    private NotifikasiService service;

    @PostMapping("/hapus/{id}")
    public void hapus(@PathVariable int id) {
        service.hapusNotif(id);
    }
}
