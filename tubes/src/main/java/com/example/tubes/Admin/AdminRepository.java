package com.example.tubes.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    List<Admin> findAll();

    Optional<Admin> findByEmail(String email);

    void save(Admin admin);

    void update(Admin admin);

    void delete(int id);
}