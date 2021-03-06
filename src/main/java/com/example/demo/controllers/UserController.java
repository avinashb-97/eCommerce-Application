package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private Logger log = LoggerFactory.getLogger(UserController.class);


	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		if(createUserRequest.getPassword().length() < 7 ){
			log.error("[CREATE USER] [Fail]  user -> " + user.getUsername() +", REASON -> invalid password");
			return ResponseEntity.badRequest().body("Password must have at least 7 characters");
		}else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.error("[CREATE USER] [Fail]  user -> " + user.getUsername() +", REASON -> password mismatch");
			return ResponseEntity.badRequest().body("Password does not match confirm password");
		}
		String encodedPassword = bCryptPasswordEncoder.encode(createUserRequest.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		log.info("[CREATE USER] [Success] user -> " + user.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
