package com.example.tubes;

import com.example.tubes.Auth.User;
import com.example.tubes.JadwalBimbingin.JadwalBimbinganService;
import com.example.tubes.Mahasiswa.MahasiswaController;
import com.example.tubes.Mahasiswa.MahasiswaService;
import com.example.tubes.Notifikasi.NotifikasiService;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
        user.setId(2);                      // <-- id mahasiswa = 2
        user.setRole("mahasiswa");
        session.setAttribute("currentUser", user);

        // 2. Mock service yg dipanggil controller
        when(mahasiswaService.getNamaDosenPembimbing(2)).thenReturn(Optional.of("Dosen A"));
        when(mahasiswaService.getIdDosenPembimbing(2)).thenReturn(22);           // <-- id dosen = 22

        String tanggal = "2025-12-10";
        String jamMulai = "09:00";
        String jamSelesai = "10:00";

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // 3. Panggil method controller
        String viewName = controller.ajukanBimbingan(
                session,
                tanggal,
                jamMulai,
                jamSelesai,
                redirectAttributes
        );

        // 4a. Cek redirect
        assertEquals("redirect:/mahasiswa/pengajuan", viewName);

        // 4b. Verifikasi input ke jadwalService
        verify(jadwalService, times(1)).insertPengajuan(
                2,                          // idMhs
                22,                         // idDosen
                LocalDate.parse(tanggal),
                LocalTime.parse(jamMulai),
                LocalTime.parse(jamSelesai)
        );

        // 4c. Verifikasi notifikasi ke dosen
        verify(notifikasiService, times(1)).buatNotif(
                22,
                "Pengajuan Baru",
                "Mahasiswa mengajukan bimbingan baru."
        );

        verify(redirectAttributes, times(1)).addFlashAttribute(eq("successMessage"), anyString());

        // 4d. Verifikasi pemanggilan service pendukung
        verify(mahasiswaService, times(1)).getNamaDosenPembimbing(2);
        verify(mahasiswaService, times(1)).getIdDosenPembimbing(2);
    }

    @Test
    void testAjukanBimbingan_UserBelumLogin() {

        HttpSession session = new MockHttpSession();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        String viewName = controller.ajukanBimbingan(
                session,
                "2025-12-10",
                "09:00",
                "10:00", 
                redirectAttributes
        );

        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(mahasiswaService, jadwalService, notifikasiService);
        verifyNoInteractions(redirectAttributes);
    }
}
