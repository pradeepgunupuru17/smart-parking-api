package com.pradeep.smart_parking_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pradeep.smart_parking_api.Repository.UsersRepository;
import com.pradeep.smart_parking_api.model.Users;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner seedAdmin(UsersRepository repo, PasswordEncoder encoder,
            @Value("${admin.email:admin@parking.com}") String email,
            @Value("${admin.password:Admin@123}") String password) {
        return args -> {
            if (repo.findByEmail(email) == null) {
                Users admin = new Users();
                admin.setName("Admin");
                admin.setEmail(email);
                admin.setPassword(encoder.encode(password));
                admin.setRole("ADMIN");
                repo.save(admin);
            }
        };
    }
}