package com.fieldcommand.internal_request;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class InternalRequestService {

    private InternalRequestRepository irr;

    public InternalRequestService(InternalRequestRepository irr) {
        this.irr = irr;
    }

    public void save(RequestModel model) throws ConstraintViolationException {
        this.irr.save(model);
    }

    public void delete(Long id) {
        this.irr.delete(id);
    }

    public void update(RequestModel newModel, Long updatableId) {
        RequestModel oldModel = this.irr.findOne(updatableId);
        oldModel.setTitle(newModel.getTitle());
        oldModel.setMessage(newModel.getMessage());
        this.irr.save(oldModel);
    }
}
