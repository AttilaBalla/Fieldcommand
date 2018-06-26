package com.fieldcommand.controller;

import com.fieldcommand.internal_request.InternalRequestService;
import com.fieldcommand.internal_request.RequestModel;
import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.user.UserPrincipal;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import com.fieldcommand.utility.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@RestController
public class InternalController {

    private static final Logger logger = LoggerFactory.getLogger(InternalController.class);

    private final InternalRequestService irs;

    @Autowired
    public InternalController(InternalRequestService irs) {
        this.irs = irs;
    }

    //TODO : You shouldn't let unauthorized users make internal requests.

    @PostMapping(value = "/api/user/ir/create")
    public ResponseEntity<?> internalRequest(@RequestBody RequestModel internalRequest, Authentication authentication) {
        System.out.println(internalRequest.toString());
        logger.info("Internal request create request");
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getId();
            this.irs.save(internalRequest, userId);
            logger.info("Internal request saved to the database");
        } catch (ConstraintViolationException e) {
            if (internalRequest.getContent().equals("")) {
                logger.error("Internal request cannot be saved with an empty message");
                return ResponseEntity.status(401).body(new GenericResponseJson(false, "You should write a message"));
            } else {
                logger.error("Internal request cannot be saved without author");
                return ResponseEntity.status(401).body(new GenericResponseJson(false, "Servers down, sorry"));
            }
        }
        return ResponseEntity.ok(new GenericResponseJson(true));
    }

    @GetMapping(value = "/api/user/ir/get")
    public String getInternalRequests() {
        return JsonUtil.toJson(irs.findAll());
    }

    @GetMapping(value = "/api/user/ir/get/{id}")
    public String getInternalRequest(@PathVariable Long id) {
        return JsonUtil.toJson(irs.findOne(id));
    }

    @PostMapping("/api/user/ir/update")
    public ResponseEntity<?> updateInternalRequest(@RequestBody RequestModel update, Authentication authentication) {

        GenericResponseJson response = new GenericResponseJson();
        try {
            irs.update(update, authentication.getName());

        } catch (UnauthorizedModificationException e) {
            e.printStackTrace();
        }

        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping(value = "/api/user/ir/delete/{id}")
    public ResponseEntity<?> internalRequestDelete(@PathVariable Long id, Authentication authentication) {
        logger.info("Internal request delete request");
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        try {
            this.irs.delete(id, userId);
        } catch (UnauthorizedModificationException e) {
            ResponseEntity.status(403).body(e.getMessage());
        }
        logger.info("Internal request deleted");
        return ResponseEntity.status(200).body(new GenericResponseJson(true));
    }
}
