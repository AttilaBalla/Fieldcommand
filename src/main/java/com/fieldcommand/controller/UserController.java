package com.fieldcommand.controller;

import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.payload.user.InviteJson;
import com.fieldcommand.payload.user.KeyPasswordJson;
import com.fieldcommand.payload.user.UpdateJson;
import com.fieldcommand.user.User;
import com.fieldcommand.user.UserPrincipal;
import com.fieldcommand.user.UserService;
import com.fieldcommand.utility.Exception.UserNotFoundException;
import com.fieldcommand.utility.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@CrossOrigin
@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value =  "/api/user/currentUser")
    public UserPrincipal getLoggedInUser(@AuthenticationPrincipal UserPrincipal loggedInUser) {
        return loggedInUser;
    }

    @PostMapping(value = "/api/admin/invite")
    public String inviteUser(@RequestBody InviteJson invite) {

        String internalError = "An internal error occurred. Try again later!";

        GenericResponseJson response = new GenericResponseJson();

        String email = invite.getEmail();
        String username = invite.getUsername();

        response = userService.validateInvite(email, username, response);
        if(!response.isSuccess()) {
            return JsonUtil.toJson(response);
        }

        boolean registerSuccess = false;
        try {

            registerSuccess = userService.registerUser(new User(email, username));

        } catch (RoleNotFoundException ex) {
            logger.error("Failed to set role for {}, reason: {}", username, ex.getMessage());
            response.setSuccess(false);
            response.setInformation(internalError);

        } catch (MailException ex) {
            logger.error("Failed to send e-mail to {}, reason: {}", email, ex.getMessage());
            response.setSuccess(false);
            response.setInformation(internalError);
        }

        if (registerSuccess) {
            response.setSuccess(true);

        } else {
            response.setSuccess(false);
            response.setInformation(internalError);
        }

        return JsonUtil.toJson(response);
    }

    @PostMapping(value = "/api/activate")
    public String activateAccount(@RequestBody KeyPasswordJson keyPasswordJson) {

        String message;
        String password = keyPasswordJson.getPassword();
        String key = keyPasswordJson.getKey();

        if(password.length() > 5) {

            try {
                userService.activateUser(key, password);

            } catch(UserNotFoundException ex) {

                message = "No corresponding user found or invalid key!";
                return JsonUtil.toJson(new GenericResponseJson(false, message));
            }

        } else {
            message = "Password has to contain at least 6 characters!";
            return JsonUtil.toJson(new GenericResponseJson(false, message));
        }

        return JsonUtil.toJson(new GenericResponseJson(true));
    }

    @PostMapping("/api/admin/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UpdateJson updateJson) {

        //TODO access validation

        logger.info(updateJson.toString());

        GenericResponseJson response = new GenericResponseJson();
        try {
            userService.updateUser(updateJson);
        } catch (UserNotFoundException ex) {

            response.setSuccess(false);
            response.setInformation("No such user exists!");
            return ResponseEntity.badRequest().body(response);

        } catch (IllegalArgumentException ex) {

            response.setSuccess(false);
            response.setInformation(ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        }

        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/api/admin/users")
    public String getUsers() {
        return JsonUtil.toJson(userService.findAll());
    }

    @GetMapping(value = "/api/admin/userRoles")
    public String getUserRoles() {
        return JsonUtil.toJson(userService.findAllRolesOfAllUsers());
    }
}
