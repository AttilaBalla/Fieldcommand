package com.fieldcommand.internal_request;

import com.fieldcommand.user.User;
import com.fieldcommand.user.UserRepository;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class InternalRequestService {

    private InternalRequestRepository irr;
    private UserRepository userRepository;

    public InternalRequestService(InternalRequestRepository irr, UserRepository userRepository) {
        this.irr = irr;
        this.userRepository = userRepository;
    }

    public void save(RequestModel model, Long userId) throws ConstraintViolationException {
        User author = userRepository.findUserById(userId);
        model.setUserId(author.getId());
        model.setStatus(InternalRequestStatus.NEW);
        this.irr.save(model);
    }

    public void delete(Long id) {
        this.irr.delete(id);
    }

    public void update(RequestModel newModel, Long updatableId) {
        RequestModel oldModel = this.irr.findOne(updatableId);
        oldModel.setTitle(newModel.getTitle());
        oldModel.setContent(newModel.getContent());
        this.irr.save(oldModel);
    }
}
