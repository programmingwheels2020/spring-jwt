package com.lenin.springjwtdemo;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/")
	public String home() {
		return "Hello World";
	}

	@PostMapping("/authenticate")
	public AuthenticationResponse createAuthentication(@RequestBody AuthenticationRequest authRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
          throw new Exception("Username and password not found",e);
		}
		
		UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
		
		String jwt = jwtUtil.generateToken(userDetails);
		return new AuthenticationResponse(jwt);
		

	}
}
