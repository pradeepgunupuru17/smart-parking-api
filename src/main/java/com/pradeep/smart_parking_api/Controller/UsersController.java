package com.pradeep.smart_parking_api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pradeep.smart_parking_api.Service.UsersService;
import com.pradeep.smart_parking_api.dto.RegisterRequest;
import com.pradeep.smart_parking_api.model.Users;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UsersController {

	@Autowired
	private UsersService usersService;

	// Register a new user (public) - role is always set to USER in the service
	@PostMapping
	public Users createUser(@Valid @RequestBody RegisterRequest request) {
		Users user = new Users();
		user.setName(request.name());
		user.setEmail(request.email());
		user.setPassword(request.password());
		return usersService.createUser(user);
	}

	// Get user by ID
	@GetMapping("/{id}")
	public Users getUserById(@PathVariable Long id) {
		return usersService.getUserById(id);
	}

	// Get all users
	@GetMapping
	public List<Users> getAllUsers() {
		return usersService.getAllUsers();
	}

	// Update user
	@PutMapping("/{id}")
	public Users updateUser(@PathVariable Long id, @RequestBody Users user) {
		return usersService.updateUser(id, user);
	}

	// Delete user
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		usersService.deleteUser(id);
	}

	// Get user by email
	@GetMapping("/email/{email}")
	public Users getUserByEmail(@PathVariable String email) {
		return usersService.getUserByEmail(email);
	}
}