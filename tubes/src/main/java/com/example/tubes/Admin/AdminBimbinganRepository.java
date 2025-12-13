package com.example.tubes.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminBimbinganRepository {
    List<AdminRiwayatBimbinganRow> findRiwayatAll();              // list riwayat
    Optional<AdminDetailBimbinganView> findDetailByIdJadwal(int idJadwal); // detail
}
