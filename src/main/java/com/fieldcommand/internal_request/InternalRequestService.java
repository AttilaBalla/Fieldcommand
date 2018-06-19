package com.fieldcommand.internal_request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class InternalRequestService {

    @Autowired
    InternalRequestRepository irr;

    public void save(RequestModel model) throws ConstraintViolationException {
        irr.save(model);
    }
}
