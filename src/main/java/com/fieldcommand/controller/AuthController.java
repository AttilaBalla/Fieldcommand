package com.fieldcommand.controller;

import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.payload.jwt.JwtAuthResponse;
import com.fieldcommand.payload.user.KeyPasswordJson;
import com.fieldcommand.payload.user.LoginJson;
import com.fieldcommand.role.RoleRepository;
import com.fieldcommand.security.JwtTokenProvider;
import com.fieldcommand.user.UserRepository;
import com.fieldcommand.user.UserService;
import com.fieldcommand.utility.Exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider tokenProvider;

    private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider,
                          UserService userService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginJson loginJson) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginJson.getUsername(),
                        loginJson.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponse(jwt));
    }

    @PostMapping(value = "/activate")
    public ResponseEntity<?> activateAccount(@RequestBody KeyPasswordJson keyPasswordJson) {

        String message;
        String password = keyPasswordJson.getPassword();
        String key = keyPasswordJson.getKey();

        if(password.length() > 5) {

            try {
                userService.activateUser(key, password);

            } catch(UserNotFoundException ex) {

                message = "No corresponding user found or invalid key!";
                return ResponseEntity.badRequest().body(new GenericResponseJson(false, message));
            }

        } else {
            message = "Password has to contain at least 6 characters!";
            return ResponseEntity.badRequest().body(new GenericResponseJson(false, message));
        }

        return ResponseEntity.ok(new GenericResponseJson(true));
    }
}
