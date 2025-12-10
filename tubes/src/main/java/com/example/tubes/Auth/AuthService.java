package com.example.tubes.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    public User authenticate(String email, String password) {
        Optional<User> userOpt = authRepository.findUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Cek Password (disini masih plain text, nanti bisa pakai BCrypt)
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Login Gagal
    }
}