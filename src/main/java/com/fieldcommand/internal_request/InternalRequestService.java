package com.fieldcommand.internal_request;

import com.fieldcommand.role.RoleType;
import com.fieldcommand.user.User;
import com.fieldcommand.user.UserRepository;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InternalRequestService {

    private InternalRequestRepository internalRequestRepository;
    private UserRepository userRepository;

    @Autowired
    public InternalRequestService(InternalRequestRepository internalRequestRepository, UserRepository userRepository) {
        this.internalRequestRepository = internalRequestRepository;
        this.userRepository = userRepository;
    }

    public void save(RequestModel model, Long userId) throws TransactionSystemException {

        User author = userRepository.findUserById(userId);

        model.setUserId(author.getId());
        model.setStatus(InternalRequestStatus.NEW);

        this.internalRequestRepository.save(model);
    }

    public void delete(Long id, Long userId) throws UnauthorizedModificationException {

        if (userRepository.findUserById(userId).getRole().getRoleType().equals(RoleType.ROLE_OWNER) ||
                userRepository.findUserById(userId).getRole().getRoleType().equals(RoleType.ROLE_ADMIN) ||
                userId.equals(internalRequestRepository.findOne(id).getUserId())) {
            this.internalRequestRepository.delete(id);
        } else {
            throw new UnauthorizedModificationException("You have no permission to modify this post.");
        }
    }

    public void updateIntRequest(RequestModel newModel, String updaterName) throws UnauthorizedModificationException {

        User updater = userRepository.findUserByUsername(updaterName);

        RequestModel updateModel = internalRequestRepository.findOne(newModel.getId());

        if (updater != userRepository.findUserById(updateModel.getUserId())) {
            throw new UnauthorizedModificationException("You are not the author of this post.");
        }
        updateModel.setTitle(newModel.getTitle());
        updateModel.setContent(newModel.getContent());

        this.internalRequestRepository.save(updateModel);
    }

    public List<HashMap<String, String>> findAll() {
        List<RequestModel> internalRequests = internalRequestRepository.findAllByOrderByIdDesc();
        List<HashMap<String, String>> internalRequestData = new ArrayList<>();

        for (RequestModel internalRequest : internalRequests) {
            internalRequestData.add(makeInternalRequestHashMap(internalRequest));
        }

        return internalRequestData;
    }


    private HashMap<String, String> makeInternalRequestHashMap(RequestModel internalRequest) {
        HashMap<String, String> internalRequestHashMap = new HashMap<>();

        internalRequestHashMap.put("id", internalRequest.getId().toString());
        internalRequestHashMap.put("title", internalRequest.getTitle());
        internalRequestHashMap.put("content", internalRequest.getContent());
        internalRequestHashMap.put("owner", userRepository.findUserById(internalRequest.getUserId()).getUsername());
        internalRequestHashMap.put("date", internalRequest.getDate());
        internalRequestHashMap.put("status", (internalRequest.getStatus().toString()));

        return internalRequestHashMap;
    }

    public HashMap<String, String> findOne(Long id) {
        RequestModel ir = internalRequestRepository.findOne(id);

        return makeInternalRequestHashMap(ir);
    }
}
