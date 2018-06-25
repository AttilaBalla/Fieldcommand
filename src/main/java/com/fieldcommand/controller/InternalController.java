package com.fieldcommand.controller;

import com.fieldcommand.internal_request.InternalRequestService;
import com.fieldcommand.internal_request.RequestModel;
import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(value = "/api/user/ir/update/{id}")
    public ResponseEntity<?> internalRequestUpdate(@RequestBody RequestModel update, @PathVariable Long id) {
        logger.info("Internal request update");
        this.irs.update(update, id);
        logger.info("internal request updated");
        return ResponseEntity.status(200).body(new GenericResponseJson(true));
    }

    @PostMapping(value = "/api/user/ir/delete/{id}")
    public ResponseEntity<?> internalRequestDelete(@PathVariable Long id) {
        logger.info("Internal request delete request");
        this.irs.delete(id);
        logger.info("Internal request deleted");
        return ResponseEntity.status(200).body(new GenericResponseJson(true));
    }
}
