package com.fieldcommand.controller;

import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.payload.jwt.JwtAuthResponse;
import com.fieldcommand.payload.user.KeyPasswordJson;
import com.fieldcommand.payload.user.LoginJson;
import com.fieldcommand.role.RoleRepository;
import com.fieldcommand.security.JwtTokenProvider;
import com.fieldcommand.user.User;
import com.fieldcommand.user.UserRepository;
import com.fieldcommand.user.UserService;
import com.fieldcommand.utility.Exception.UserNotFoundException;
import com.fieldcommand.utility.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private JwtTokenProvider tokenProvider;

    private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          JwtTokenProvider tokenProvider,
                          UserService userService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
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
        String username = keyPasswordJson.getUsername();

        if(password.length() > 5) {

            try {
                userService.activateUser(key, password, username);

            } catch(UserNotFoundException | IllegalArgumentException ex) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseJson(false, ex.getMessage()));

            }

        } else {
            message = "Password has to contain at least 6 characters!";

            return ResponseEntity.badRequest().body(new GenericResponseJson(false, message));
        }

        return ResponseEntity.ok(new GenericResponseJson(true));
    }

    @PostMapping(value = "/validateKey")
    public String validateActivationKey(@RequestBody String key) {

        // comes across as "key", the "-s need to be removed
        User user = userRepository.findUserByActivationKey(key.substring(1, key.length() - 1));

        if(user != null) {

            return JsonUtil.toJson(new GenericResponseJson(true, user.getUsername()));

        } else {

            return JsonUtil.toJson(new GenericResponseJson(false, ""));
        }
    }
}
