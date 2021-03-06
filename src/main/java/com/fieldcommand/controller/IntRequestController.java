package com.fieldcommand.controller;

import com.fieldcommand.intrequest.InternalRequestService;
import com.fieldcommand.intrequest.InternalRequest;
import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.payload.intrequest.RequestStatusJson;
import com.fieldcommand.payload.intrequest.RequestSupportJson;
import com.fieldcommand.user.UserPrincipal;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import com.fieldcommand.utility.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class IntRequestController {

    private static final Logger logger = LoggerFactory.getLogger(IntRequestController.class);

    private final InternalRequestService internalRequestService;

    @Autowired
    public IntRequestController(InternalRequestService internalRequestService) {

        this.internalRequestService = internalRequestService;
    }

    @PostMapping(value = "/user/ir/create")
    public ResponseEntity<?> createInternalRequest(@RequestBody InternalRequest internalRequest, Authentication authentication) {

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

    @GetMapping(value = "/user/ir/get")
    public String getInternalRequests() {
        return JsonUtil.toJson(internalRequestService.findAll());
    }

    @GetMapping(value = "/user/ir/get/{id}")
    public String getInternalRequest(@PathVariable Long id) {
        try {

            return JsonUtil.toJson(internalRequestService.findOne(id));

        } catch(IllegalArgumentException ex) {

            return JsonUtil.toJson(new GenericResponseJson(false, ex.getMessage()));
        }
    }

    @PutMapping("/user/ir/update")
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

    @PostMapping("/user/ir/support")
    public ResponseEntity<?> handleSupport(@RequestBody RequestSupportJson requestSupportJson) {

        HashMap response;

        try {

            response = internalRequestService.handleSupporter(requestSupportJson);

        } catch (UnauthorizedModificationException ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseJson(false, ex.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/user/ir/delete/{id}")
    public ResponseEntity<?> deleteInternalRequest(@PathVariable Long id, Authentication authentication) {

        try {

            internalRequestService.delete(id, authentication);

        } catch (UnauthorizedModificationException ex) {

            return ResponseEntity.status(403).body(new GenericResponseJson(false, ex.getMessage()));
        }

        return ResponseEntity.status(200).body(new GenericResponseJson(true));
    }

    @PutMapping(value = "/user/ir/updateStatus")
    public ResponseEntity<?> updateIntRequestStatus(@RequestBody RequestStatusJson requestStatusJson, Authentication authentication) {

        try {

            internalRequestService.updateIntRequestStatus(requestStatusJson, authentication.getName());

        } catch (UnauthorizedModificationException ex) {

            return ResponseEntity.status(403).body(new GenericResponseJson(false, ex.getMessage()));
        }

        return ResponseEntity.status(200).body(new GenericResponseJson(true));

    }
}
