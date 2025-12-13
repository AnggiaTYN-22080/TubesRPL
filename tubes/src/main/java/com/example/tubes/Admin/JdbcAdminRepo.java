package com.example.tubes.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAdminRepo implements AdminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mapper untuk mengubah hasil SQL menjadi Object Admin
    private Admin mapRowToAdmin(ResultSet rs, int rowNum) throws SQLException {
    return new Admin(
            rs.getInt("id"),
            rs.getString("nama"),      // <-- ini yang benar (bukan "name")
            rs.getString("email"),
            rs.getString("password")
    );
}

    @Override
    public List<Admin> findAll() {
        return jdbcTemplate.query("SELECT * FROM admin", this::mapRowToAdmin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email = ?";
        List<Admin> results = jdbcTemplate.query(sql, this::mapRowToAdmin, email);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public void save(Admin admin) {
        jdbcTemplate.update("INSERT INTO admin (nama, email, password) VALUES (?, ?, ?)",
                admin.getNama(), admin.getEmail(), admin.getPassword());
    }

    @Override
    public void update(Admin admin) {
        jdbcTemplate.update("UPDATE admin SET nama=?, email=?, password=? WHERE id=?",
                admin.getNama(), admin.getEmail(), admin.getPassword(), admin.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM admin WHERE id=?", id);
    }
}