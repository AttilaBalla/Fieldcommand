package com.fieldcommand.controller;

import com.fieldcommand.internal_request.InternalRequestService;
import com.fieldcommand.internal_request.RequestModel;
import com.fieldcommand.payload.GenericResponseJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

@RestController
public class InternalController {

    private static final Logger logger = LoggerFactory.getLogger(InternalController.class);

    @Autowired
    InternalRequestService irs;

    @PostMapping(value = "/internal/request")
    public ResponseEntity<?> internalRequest(@RequestBody RequestModel internalRequest) {
        logger.info("Internal request catched");
        try {
            irs.save(internalRequest);
            logger.info("Internal request saved to the database");
        } catch (ConstraintViolationException e) {
            if (internalRequest.getMessage().equals("")) {
                logger.error("Internal request cannot be saved with an empty message");
                return ResponseEntity.status(401).body(new GenericResponseJson(false, "You should write a message"));
            } else {
                logger.error("Internal request cannot be saved without author");
                return ResponseEntity.status(401).body(new GenericResponseJson(false, "Servers down, sorry"));
            }
        }
        return ResponseEntity.ok(new GenericResponseJson(true));
    }
}
