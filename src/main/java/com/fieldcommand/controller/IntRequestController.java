package com.fieldcommand.controller;

import com.fieldcommand.intrequest.InternalRequestService;
import com.fieldcommand.intrequest.InternalRequest;
import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.user.UserPrincipal;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import com.fieldcommand.utility.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

@RestController
public class IntRequestController {

    private static final Logger logger = LoggerFactory.getLogger(IntRequestController.class);

    private final InternalRequestService internalRequestService;

    @Autowired
    public IntRequestController(InternalRequestService internalRequestService) {

        this.internalRequestService = internalRequestService;
    }

    @PostMapping(value = "/api/user/ir/create")
    public ResponseEntity<?> internalRequest(@RequestBody InternalRequest internalRequest, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        try {
            this.internalRequestService.save(internalRequest, userId);

        } catch (TransactionSystemException e) {

            if (internalRequest.getContent().equals("")) {
                return ResponseEntity.status(401).body(new GenericResponseJson(false, "You should write a message"));
            } else {
                return ResponseEntity.status(401).body(new GenericResponseJson(false, "Internal server error occurred. Please try again later."));
            }
        }

        return ResponseEntity.ok(new GenericResponseJson(true));
    }

    @GetMapping(value = "/api/user/ir/get")
    public String getInternalRequests() {
        return JsonUtil.toJson(internalRequestService.findAll());
    }

    @GetMapping(value = "/api/user/ir/get/{id}")
    public String getInternalRequest(@PathVariable Long id) {
        return JsonUtil.toJson(internalRequestService.findOne(id));
    }

    @PostMapping("/api/user/ir/update")
    public ResponseEntity<?> updateInternalRequest(@RequestBody InternalRequest update, Authentication authentication) {

        GenericResponseJson response = new GenericResponseJson();
        try {
            internalRequestService.updateIntRequest(update, authentication.getName());

        } catch (UnauthorizedModificationException ex) {

            return ResponseEntity.badRequest().body(new GenericResponseJson(
                    false, "You are not authorized to update this request."));
        }

        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping(value = "/api/user/ir/delete/{id}")
    public ResponseEntity<?> internalRequestDelete(@PathVariable Long id, Authentication authentication) {

        try {

            this.internalRequestService.delete(id, authentication);

        } catch (UnauthorizedModificationException e) {
            ResponseEntity.status(403).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(new GenericResponseJson(true));
    }
}
