package com.example.tubes.JadwalBimbingin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JadwalBimbingan {
    private int id;
    private String dosenId;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuSelesai;
    private String lokasi;
    private String status;
}