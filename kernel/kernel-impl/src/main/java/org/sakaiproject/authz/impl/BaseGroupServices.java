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
public class BaseGroupServices {

    private EntityManager entityManager;
    private DbAuthzGroupService.DbStorage storage;
    private BaseAuthzGroupService service;
    private TimeService timeService;
    private UserDirectoryService userDirectoryService;

    public Reference newReference(String id) {
        return entityManager.newReference(id);
    }

    public void completeGet(BaseAuthzGroup azGroup) {
        storage.completeGet(azGroup);
    }

    public String getAccessPoint(boolean b) {
        return service.getAccessPoint(b);
    }

    public String authzGroupReference(String id) {
        return service.authzGroupReference(id);
    }

    public Time newTimeGmt(String time) {
        return timeService.newTimeGmt(time);
    }

    public User getUser(String userId) throws UserNotDefinedException {
        return userDirectoryService.getUser(userId);
    }

    public User getAnonymousUser() {
        return userDirectoryService.getAnonymousUser();
    }

    public String getUserEid(String userId) throws UserNotDefinedException {
        return userDirectoryService.getUserEid(userId);
    }

    public Time newTime() {
        return timeService.newTime();
    }
}
