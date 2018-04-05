package com.fieldcommand.controller;

import com.fieldcommand.model.json.InviteJson;
import org.springframework.web.bind.annotation.*;


@RestController
public class ApiController {

    @PostMapping(value = "/admin/invite")
    public String inviteUser(@RequestBody InviteJson invite) {
        System.out.println(invite.getEmail() + " " + invite.getUsername());
        // TODO handle validation, persist, email sending here

        // then return OK or not OK
        return "success";
    }

}
