package com.pradeep.smart_parking_api.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pradeep.smart_parking_api.Repository.UsersRepository;
import com.pradeep.smart_parking_api.exception.DuplicateEmailException;
import com.pradeep.smart_parking_api.exception.ResourceNotFoundException;
import com.pradeep.smart_parking_api.model.Users;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public Users createUser(Users user) {
		if (usersRepository.findByEmail(user.getEmail()) != null) {
			throw new DuplicateEmailException("Email already registered: " + user.getEmail());
		}
		user.setRole("USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return usersRepository.save(user);
	}

	@Override
	public Users getUserById(Long id) {
		return usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
	}

	@Override
	public List<Users> getAllUsers() {
		return usersRepository.findAll();
	}

	@Override
	public Users updateUser(Long id, Users user) {
		Users existingUser = usersRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

		existingUser.setName(user.getName());
		existingUser.setEmail(user.getEmail());
		existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
		existingUser.setRole(user.getRole());

		return usersRepository.save(existingUser);
	}

	@Override
	public void deleteUser(Long id) {
		Users existingUser = usersRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
		usersRepository.delete(existingUser);
	}

	@Override
	public Users getUserByEmail(String email) {
		return usersRepository.findByEmail(email);
	}
}