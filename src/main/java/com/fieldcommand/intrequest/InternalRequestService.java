package com.fieldcommand.intrequest;

import com.fieldcommand.payload.intrequest.RequestStatusJson;
import com.fieldcommand.payload.intrequest.RequestSupportJson;
import com.fieldcommand.project.ProjectRepository;
import com.fieldcommand.user.User;
import com.fieldcommand.user.UserRepository;
import com.fieldcommand.user.UserService;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InternalRequestService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private InternalRequestRepository internalRequestRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public InternalRequestService(InternalRequestRepository internalRequestRepository,
                                  UserRepository userRepository,
                                  ProjectRepository projectRepository,
                                  UserService userService) {
        this.internalRequestRepository = internalRequestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    public void save(InternalRequest intRequest, Long userId) throws TransactionSystemException {

        User owner = userRepository.findUserById(userId);

        intRequest.setOwner(owner);
        intRequest.setProject(projectRepository.findByShortName(intRequest.getProjectName()));
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

    public void updateIntRequestStatus(RequestStatusJson requestStatusJson, String updaterName) throws UnauthorizedModificationException {

        User updater = userRepository.findUserByUsername(updaterName);

        InternalRequest internalRequest = internalRequestRepository.findOne(requestStatusJson.getRequestId());

        if(updater.getProjects().stream().noneMatch(project -> project == internalRequest.getProject())) {
            throw new UnauthorizedModificationException("Failed to update status, you are not assigned to this project!");
        }


        internalRequest.setStatus(InternalRequestStatus.valueOf(requestStatusJson.getStatus()));

        String response = requestStatusJson.getResponse();

        if(response.length() > 0) {
            internalRequest.setResponse(response);
        }

        internalRequestRepository.save(internalRequest);
    }

    public List<HashMap<String, Object>> findAll() {
        List<InternalRequest> internalRequests = internalRequestRepository.findAllByOrderByIdDesc();
        List<HashMap<String, Object>> internalRequestData = new ArrayList<>();

        for (InternalRequest internalRequest : internalRequests) {
            internalRequestData.add(makeInternalRequestHashMap(internalRequest));
        }

        return internalRequestData;
    }


    private HashMap<String, Object> makeInternalRequestHashMap(InternalRequest internalRequest) {
        HashMap<String, Object> internalRequestHashMap = new HashMap<>();

        internalRequestHashMap.put("id", internalRequest.getId().toString());
        internalRequestHashMap.put("title", internalRequest.getTitle());
        internalRequestHashMap.put("content", internalRequest.getContent());
        internalRequestHashMap.put("owner", internalRequest.getOwner().getUsername());
        internalRequestHashMap.put("date", internalRequest.getDate());
        internalRequestHashMap.put("status", (internalRequest.getStatus().toString()));
        internalRequestHashMap.put("project", internalRequest.getProject().getShortName());
        internalRequestHashMap.put("supportPercent", internalRequest.getSupportPercent());
        internalRequestHashMap.put("supporters", internalRequest.getSupportingUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList()));

        return internalRequestHashMap;
    }

    public HashMap<String, Object> findOne(Long id) {
        InternalRequest ir = internalRequestRepository.findOne(id);
        ir.setSupportPercent(calculateSupportPercent(ir));

        return makeInternalRequestHashMap(ir);
    }

    // returns new % after support is added/removed
    public int handleSupporter(RequestSupportJson requestSupportJson) throws UnauthorizedModificationException {

        InternalRequest intRequest = internalRequestRepository.findOne(requestSupportJson.getRequestId());
        User user = userRepository.findUserByUsername(requestSupportJson.getUsername());

        if(intRequest.getOwner() == user) {
            throw new UnauthorizedModificationException("You cannot withdraw support of a request that you made!");
        }

        Set<User> supporterSet = intRequest.getSupportingUsers();

        if(supporterSet.contains(user)) {
            supporterSet.remove(user);
        } else {
            supporterSet.add(user);
        }

        intRequest.setSupportingUsers(supporterSet);
        intRequest.setSupportPercent(calculateSupportPercent(intRequest));
        internalRequestRepository.save(intRequest);

        return intRequest.getSupportPercent();
    }

    private int calculateSupportPercent(InternalRequest internalRequest) {
        double numberOfSupporters = internalRequest.getSupportingUsers().size();
        double totalUsers = userService.getUserCount();
        return (int)Math.floor((numberOfSupporters / totalUsers) * 100);
    }
}
