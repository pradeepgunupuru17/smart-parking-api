package com.pradeep.smart_parking_api.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pradeep.smart_parking_api.model.Users;
@Service
public interface UsersService  {
	
	Users createUser(Users user);

    // Get a user by ID
    Users getUserById(Long id);

    // Get all users
    List<Users> getAllUsers();

    // Update user details
    Users updateUser(Long id, Users user);

    // Delete a user
    void deleteUser(Long id);

    // Optional: find user by email (useful for login/authentication)
    Users getUserByEmail(String email);


}
