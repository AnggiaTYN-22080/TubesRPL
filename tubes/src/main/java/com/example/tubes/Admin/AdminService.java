package com.example.tubes.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public void registerNewAdmin(Admin admin) {
        // Di sini bisa tambah logika validasi atau hash password sebelum save
        adminRepository.save(admin);
    }
}