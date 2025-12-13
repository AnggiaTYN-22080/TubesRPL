package com.example.tubes.Auth;

import com.example.tubes.Admin.Admin;
import com.example.tubes.Admin.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AdminRepository adminRepository;

    public User authenticate(String email, String password) {

        // 1) Coba login dari tabel USERS
        Optional<User> userOpt = authRepository.findUserByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return user; // role dari DB users dipakai apa adanya
            }
            return null;
        }

        // 2) Kalau tidak ada di USERS, coba login dari tabel ADMIN
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getPassword().equals(password)) {
                // bikin User session untuk admin
                return new User(
                        admin.getId(),
                        admin.getNama(),
                        admin.getEmail(),
                        admin.getPassword(),
                        "admin"
                );
            }
        }

        return null;
    }
}
