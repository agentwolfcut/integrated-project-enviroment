package dev.backendintegratedproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SharedUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getOidByEmail(String email) {
        String sql = "SELECT oid FROM itbkk_shared.users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, String.class);
        } catch (Exception e) {
            return null; // หากไม่พบ email
        }
    }
}
