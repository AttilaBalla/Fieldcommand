package com.fieldcommand.controller;

import com.fieldcommand.json.user.UpdateJson;
import com.fieldcommand.user.User;
import com.fieldcommand.json.GenericResponseJson;
import com.fieldcommand.json.user.InviteJson;
import com.fieldcommand.json.user.KeyPasswordJson;
import com.fieldcommand.swr_net.SwrNetService;
import com.fieldcommand.user.UserService;
import com.fieldcommand.utility.JsonUtil;
import com.fieldcommand.utility.Exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;


@RestController
public class ApiController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserService userService;
    private SwrNetService swrNetService;

    @Autowired
    public void setUserService(UserService userService, SwrNetService swrNetService) {
        this.userService = userService;
        this.swrNetService = swrNetService;
    }

    @PostMapping(value = "/admin/invite")
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

    @PostMapping(value = "/activate")
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

    @PostMapping("/admin/updateuser")
    public String updateUser(@RequestBody UpdateJson updateJson) {

        System.out.println(updateJson.toString());

        GenericResponseJson response = new GenericResponseJson();
        try {
            userService.updateUser(updateJson);
        } catch (UserNotFoundException ex) {

            response.setSuccess(false);
            response.setInformation("No such user exists!");

        } catch (IllegalArgumentException ex) {

            response.setSuccess(false);
            response.setInformation("Internal error occured - please notify the owner.");

        }

        response.setSuccess(true);

        return JsonUtil.toJson(response);
    }

    @GetMapping(value = "/admin/users")
    public String getUsers() {
        return JsonUtil.toJson(userService.findAll());
    }

    @GetMapping(value = "/admin/userRoles")
    public String getUserRoles() {
        return JsonUtil.toJson(userService.findAllRolesOfAllUsers());
    }

    @GetMapping(value = "/swrstatus")
    public String getSwrStatus() {
        return JsonUtil.toJson(swrNetService.getStatus());
    }


}
