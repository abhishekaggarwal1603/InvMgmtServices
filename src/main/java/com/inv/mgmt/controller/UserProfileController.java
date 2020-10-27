package com.inv.mgmt.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inv.mgmt.model.UserProfile;
import com.inv.mgmt.repo.UserProfileRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserProfileController {

	@Autowired
	UserProfileRepository userProfileRepository;
	
	

	@GetMapping("/basicauth/initialCheck")
	public ResponseEntity<String> initialCheck() {
		System.out.println("Initial Check");

		List<UserProfile> users = new ArrayList<>();
		userProfileRepository.findAll().forEach(users::add);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if(users.size()==0)
		{
			UserProfile userProfile = new UserProfile();
			userProfile.setEmail("test@stryker.com");
			userProfile.setName("Test Account");
			userProfile.setRole("ADMIN");
			userProfile.setPassword(passwordEncoder.encode("password"));
			
			userProfileRepository.save(userProfile);
			
		}

		return new ResponseEntity<String>("Initial Check Done",HttpStatus.OK);
	}
	
	
	
	@PostMapping(value = "/basicauth/validate")
	public ResponseEntity<UserProfile> validateUser(@RequestBody UserProfile userProfile) {

	Optional<UserProfile> user = userProfileRepository.findByEmail(userProfile.getEmail());
			
		if (user.isPresent()) {
			UserProfile _userProfile = user.get();
			
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(_userProfile.getPassword());
			//	System.out.println(hashedPassword);
			
		
			
		if(passwordEncoder.matches(userProfile.getPassword(), _userProfile.getPassword()))
		{
		_userProfile.setPassword("");	
		return new ResponseEntity<>(_userProfile, HttpStatus.OK);
		}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value = "/users/create")
	public UserProfile postUser(@RequestBody UserProfile userProfile) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		String hashedPassword = passwordEncoder.encode(userProfile.getPassword());
		userProfile.setPassword(hashedPassword);
			
		
		UserProfile _user = userProfileRepository.save(userProfile);
		return _user;
	}
	
	@PostMapping(value = "/users/changePassword")
	public ResponseEntity<UserProfile> changePassword(@RequestBody ArrayList<String> values) {
	//	UserProfile _user = userProfileRepository.save(userProfile);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Long userId = Long.parseLong(values.get(0)); 
		
		Optional<UserProfile> user = userProfileRepository.findById(userId);
		UserProfile _userProfile = null;
		if (user.isPresent()) {
			 _userProfile = user.get();
		}
		
		
		
	//	if(_userProfile!=null && _userProfile.getPassword().equals(values.get(1)))
		if(_userProfile!=null && passwordEncoder.matches(values.get(1),_userProfile.getPassword()))
		{
		_userProfile.setPassword(passwordEncoder.encode(values.get(2)));
	   	userProfileRepository.save(_userProfile);
		
		return new ResponseEntity<>(userProfileRepository.save(_userProfile), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}

	@GetMapping("/users")
	public List<UserProfile> getAllUsers() {
		//	System.out.println("Get all Users...");

		List<UserProfile> users = new ArrayList<>();
		userProfileRepository.findAll().forEach(users::add);

		return users;
	}
	

	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
		//	System.out.println("Delete User with ID = " + id + "...");

		userProfileRepository.deleteById(id);

		return new ResponseEntity<>("User has been deleted!", HttpStatus.OK);
	}
	
	
	@PutMapping("/users/{id}")
	public ResponseEntity<UserProfile> updateUser(@PathVariable("id") long id, @RequestBody UserProfile user) {
		
		//	System.out.println("Update User with ID = " + id + "...");
		Optional<UserProfile> userData = userProfileRepository.findById(id);

		if (userData.isPresent()) {
			UserProfile _user = userData.get();
			_user.setName(user.getName());
			_user.setEmail(user.getEmail());
			_user.setRole(user.getRole());
		//	_user.setPassword(user.getPassword());
		
						
			return new ResponseEntity<>(userProfileRepository.save(_user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/users/resetPassword/{id}")
	public ResponseEntity<UserProfile> resetPassword(@PathVariable("id") long id, @RequestBody UserProfile user) {
		
		//	System.out.println("Update User with ID = " + id + "...");
		Optional<UserProfile> userData = userProfileRepository.findById(id);

		if (userData.isPresent()) {
			UserProfile _user = userData.get();
			
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
						
			_user.setPassword(passwordEncoder.encode(user.getPassword()));
						
			return new ResponseEntity<>(userProfileRepository.save(_user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	}
