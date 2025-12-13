package com.example.tubes.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAdminDashboardRepo implements AdminDashboardRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public int countMahasiswa() {
        Integer v = jdbc.queryForObject("SELECT COUNT(*) FROM mahasiswa", Integer.class);
        return v == null ? 0 : v;
    }

    @Override
    public int countDosen() {
        Integer v = jdbc.queryForObject("SELECT COUNT(*) FROM dosen", Integer.class);
        return v == null ? 0 : v;
    }

    @Override
    public int countPengajuanAktif() {
        Integer v = jdbc.queryForObject(
                "SELECT COUNT(*) FROM jadwal_bimbingan WHERE LOWER(status) IN ('pending','approved')",
                Integer.class
        );
        return v == null ? 0 : v;
    }
}
