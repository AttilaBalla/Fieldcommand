package com.fieldcommand.controller;

import com.fieldcommand.model.User;
import com.fieldcommand.model.json.GenericResponseJson;
import com.fieldcommand.model.json.InviteJson;
import com.fieldcommand.service.UserService;
import com.fieldcommand.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ApiController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/admin/invite")
    public String inviteUser(@RequestBody InviteJson invite) {

        GenericResponseJson response = new GenericResponseJson();

        String email = invite.getEmail();
        String username = invite.getUsername();

        if(!username.equals("") || !email.equals("")) {

           User user = userService.findUserByEmail(email);

           if(user == null) {

               boolean registerSuccess = userService.registerUser(new User(email, username));

               if (registerSuccess) {
                   response.setSuccess(true);
               } else {
                   response.setSuccess(false);
                   response.setInformation("An internal error occured. Try again later!");
               }

           } else {
               response.setSuccess(false);
               response.setInformation("E-mail address already exists!");
           }

        } else {
            response.setSuccess(false);
            response.setInformation("The fields cannot be empty!");

        }

        return JsonUtil.toJson(response);
    }

}
