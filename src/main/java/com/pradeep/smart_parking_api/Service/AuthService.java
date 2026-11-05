package com.pradeep.smart_parking_api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pradeep.smart_parking_api.Repository.UsersRepository;
import com.pradeep.smart_parking_api.dto.LoginRequest;
import com.pradeep.smart_parking_api.dto.LoginResponse;
import com.pradeep.smart_parking_api.exception.InvalidCredentialsException;
import com.pradeep.smart_parking_api.model.Users;
import com.pradeep.smart_parking_api.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        Users user = usersRepository.findByEmail(request.email());

        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String role = (user.getRole() == null) ? "USER" : user.getRole().toUpperCase();
        String token = jwtUtil.generateToken(user.getEmail(), role);
        return new LoginResponse(token, role);
    }
}