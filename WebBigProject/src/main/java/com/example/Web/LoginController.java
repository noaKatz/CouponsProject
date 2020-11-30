package com.example.Web;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Login.ClientType;
import com.example.Login.LoginManager;
import com.example.exceptions.loginFailedException;
import com.example.facades.ClientFacade;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	private LoginManager manager;
	@Autowired
	private Map<String, OurSession> sessions;
	
	public LoginController(LoginManager manager) {
		super();
		this.manager = manager;
	}
	
	@PostMapping("login/{email}/{password}/{clientType}")
	public ResponseEntity<?> login(@PathVariable String email,@PathVariable String password, @PathVariable ClientType clientType) {
		try {
			ClientFacade facade= manager.login(email, password, clientType);
			String token= UUID.randomUUID().toString();
			sessions.put(token, new OurSession(facade, System.currentTimeMillis()));
			return ResponseEntity.ok(token);
		} catch (loginFailedException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}	
	}
	
	@PostMapping("logout/{token}")
	public ResponseEntity<String> logout(@PathVariable String token){
		sessions.remove(token);
		return ResponseEntity.ok("logged out");
	}
	
}
