package com.example.tubes;

import com.example.tubes.Auth.User;
import com.example.tubes.JadwalBimbingin.JadwalBimbinganService;
import com.example.tubes.Mahasiswa.MahasiswaController;
import com.example.tubes.Mahasiswa.MahasiswaService;
import com.example.tubes.Notifikasi.NotifikasiService;
import com.example.tubes.Ruangan.RuanganService; // Tambahkan ini jika perlu mocking ruangan
import com.example.tubes.JadwalKuliah.JadwalKuliahService; // Tambahkan ini karena controller memakainya

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Tidak lagi dipakai di controller

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * White box test untuk fitur:
 * Pengajuan Bimbingan dari Mahasiswa ke Dosen
 */
public class MahasiswaPengajuanTest {

    @Mock
    private MahasiswaService mahasiswaService;

    @Mock
    private JadwalBimbinganService jadwalService;

    @Mock
    private NotifikasiService notifikasiService;

    @Mock
    private JadwalKuliahService jadwalKuliahService; // Mock tambahan untuk dependensi controller

    @Mock
    private RuanganService ruanganService; // Mock tambahan untuk dependensi controller

    @InjectMocks
    private MahasiswaController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAjukanBimbingan_Berhasil() {

        // 1. Session + user login
        MockHttpSession session = new MockHttpSession();
        User user = new User();
        user.setId(2);
        user.setName("Mahasiswa Test"); // Set nama untuk notifikasi
        user.setRole("mahasiswa");
        session.setAttribute("currentUser", user);

        // 2. Mock service
        when(mahasiswaService.getIdDosenPembimbing(2)).thenReturn(22);

        // Data Input (Konversi String ke Object karena Controller minta Object)
        LocalDate tanggal = LocalDate.parse("2025-12-10");
        LocalTime jamMulai = LocalTime.parse("09:00");
        LocalTime jamSelesai = LocalTime.parse("10:00");
        int idRuangan = 101; // Dummy ID Ruangan

        // 3. Panggil method controller yang BENAR (prosesPengajuan)
        // Perhatikan parameter harus sesuai urutan di Controller
        String viewName = controller.prosesPengajuan(
                session,
                tanggal,
                jamMulai,
                jamSelesai,
                idRuangan);

        // 4a. Cek redirect
        assertEquals("redirect:/mahasiswa/riwayat", viewName);

        // 4b. Verifikasi input ke jadwalService (termasuk idRuangan)
        verify(jadwalService, times(1)).insertPengajuan(
                2, // idMhs
                22, // idDosen
                tanggal,
                jamMulai,
                jamSelesai,
                idRuangan // Parameter baru
        );

        // 4c. Verifikasi notifikasi ke dosen
        verify(notifikasiService, times(1)).buatNotif(
                22,
                "Pengajuan Bimbingan",
                "Mahasiswa Mahasiswa Test mengajukan jadwal baru.");

        // Verifikasi service pendukung
        verify(mahasiswaService, times(1)).getIdDosenPembimbing(2);
    }

    @Test
    void testAjukanBimbingan_UserBelumLogin() {

        HttpSession session = new MockHttpSession();
        // Tidak perlu RedirectAttributes karena method controller tidak menggunakannya
        // lagi

        String viewName = controller.prosesPengajuan(
                session,
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                0);

        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(mahasiswaService, jadwalService, notifikasiService);
    }
}