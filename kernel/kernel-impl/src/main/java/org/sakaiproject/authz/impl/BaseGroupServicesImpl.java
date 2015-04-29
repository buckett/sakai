package org.sakaiproject.authz.impl;

import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.api.TimeService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

/**
 * This holds references to services which are needed by entities to fulfill their API.
 * This allows easier testing and mocking of the dependent services.
 */
public class BaseGroupServicesImpl implements BaseGroupServices {

    private EntityManager entityManager;
    private DbAuthzGroupService.DbStorage storage;
    private BaseAuthzGroupService service;
    private TimeService timeService;

    public void setUserDirectoryService(UserDirectoryService userDirectoryService) {
        this.userDirectoryService = userDirectoryService;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setStorage(DbAuthzGroupService.DbStorage storage) {
        this.storage = storage;
    }

    public void setService(BaseAuthzGroupService service) {
        this.service = service;
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    private UserDirectoryService userDirectoryService;

    @Override
    public Reference newReference(String id) {
        return entityManager.newReference(id);
    }

    @Override
    public void completeGet(BaseAuthzGroup azGroup) {
        storage.completeGet(azGroup);
    }

    @Override
    public String getAccessPoint(boolean b) {
        return service.getAccessPoint(b);
    }

    @Override
    public String authzGroupReference(String id) {
        return service.authzGroupReference(id);
    }

    @Override
    public Time newTimeGmt(String time) {
        return timeService.newTimeGmt(time);
    }

    @Override
    public User getUser(String userId) throws UserNotDefinedException {
        return userDirectoryService.getUser(userId);
    }

    @Override
    public User getAnonymousUser() {
        return userDirectoryService.getAnonymousUser();
    }

    @Override
    public String getUserEid(String userId) throws UserNotDefinedException {
        return userDirectoryService.getUserEid(userId);
    }

    @Override
    public Time newTime() {
        return timeService.newTime();
    }
}
