package com.fieldcommand.intrequest;

import com.fieldcommand.user.User;
import com.fieldcommand.user.UserRepository;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

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

    public void save(InternalRequest intRequest, Long userId) throws TransactionSystemException {

        User owner = userRepository.findUserById(userId);

        intRequest.setOwner(owner);
        intRequest.setStatus(InternalRequestStatus.WAITING);

        this.internalRequestRepository.save(intRequest);
    }

    public void delete(Long id, Authentication authentication) throws UnauthorizedModificationException {

        User deleter = userRepository.findUserByUsername(authentication.getName());

        InternalRequest internalRequest = internalRequestRepository.findOne(id);

        if (internalRequest.getOwner() == deleter) {
            this.internalRequestRepository.delete(id);
        } else {
            throw new UnauthorizedModificationException("You have no permission to delete this internal request!");
        }
    }

    public void updateIntRequest(InternalRequest newModel, String updaterName) throws UnauthorizedModificationException {

        User updater = userRepository.findUserByUsername(updaterName);

        InternalRequest updateModel = internalRequestRepository.findOne(newModel.getId());

        if (updater != userRepository.findUserById(updateModel.getOwner().getId())) {
            throw new UnauthorizedModificationException("You are not the author of this post.");
        }
        updateModel.setTitle(newModel.getTitle());
        updateModel.setContent(newModel.getContent());

        this.internalRequestRepository.save(updateModel);
    }

    public List<HashMap<String, String>> findAll() {
        List<InternalRequest> internalRequests = internalRequestRepository.findAllByOrderByIdDesc();
        List<HashMap<String, String>> internalRequestData = new ArrayList<>();

        for (InternalRequest internalRequest : internalRequests) {
            internalRequestData.add(makeInternalRequestHashMap(internalRequest));
        }

        return internalRequestData;
    }


    private HashMap<String, String> makeInternalRequestHashMap(InternalRequest internalRequest) {
        HashMap<String, String> internalRequestHashMap = new HashMap<>();

        internalRequestHashMap.put("id", internalRequest.getId().toString());
        internalRequestHashMap.put("title", internalRequest.getTitle());
        internalRequestHashMap.put("content", internalRequest.getContent());
        internalRequestHashMap.put("owner", internalRequest.getOwner().getUsername());
        internalRequestHashMap.put("date", internalRequest.getDate());
        internalRequestHashMap.put("status", (internalRequest.getStatus().toString()));
        internalRequestHashMap.put("project", internalRequest.getProject());

        return internalRequestHashMap;
    }

    public HashMap<String, String> findOne(Long id) {
        InternalRequest ir = internalRequestRepository.findOne(id);

        return makeInternalRequestHashMap(ir);
    }
}
