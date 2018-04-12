package com.fieldcommand.controller;

import com.fieldcommand.model.User;
import com.fieldcommand.model.json.GenericResponseJson;
import com.fieldcommand.model.json.InviteJson;
import com.fieldcommand.service.ApiService;
import com.fieldcommand.service.RoleService;
import com.fieldcommand.service.UserService;
import com.fieldcommand.util.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;


@RestController
public class ApiController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${swrnet.rotr-internal.url}")
    private String url;

    private UserService userService;
    private RoleService roleService;
    private ApiService apiService;

    @Autowired
    public void setUserService(UserService userService, RoleService roleService, ApiService apiService) {
        this.userService = userService;
        this.roleService = roleService;
        this.apiService = apiService;
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

    @GetMapping(value = "/admin/users")
    public String getUsers() {
        return JsonUtil.toJson(userService.findAll());
    }

    @GetMapping(value = "/admin/roles")
    public String getRoles() {
        return JsonUtil.toJson(roleService.findAll());
    }

    @GetMapping(value = "/admin/userRoles")
    public String getUserRoles() {
        return JsonUtil.toJson(userService.findAllRolesOfAllUsers());
    }

    @GetMapping(value = "/swrstatus")
    public String getSwrStatus() {


        JSONObject status = new JSONObject();
        try {
            status = apiService.getJson(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JsonUtil.toJson(status);
    }


}
