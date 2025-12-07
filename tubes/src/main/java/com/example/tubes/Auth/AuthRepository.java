package com.example.tubes.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<User> findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    rs.getInt("idUser"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")), email);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Email tidak ditemukan
        }
    }
}