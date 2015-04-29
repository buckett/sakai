package org.sakaiproject.authz.impl;

import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserNotDefinedException;

/**
 * Created by buckett on 29/04/15.
 */
public interface BaseGroupServices {
    Reference newReference(String id);

    void completeGet(BaseAuthzGroup azGroup);

    String getAccessPoint(boolean b);

    String authzGroupReference(String id);

    Time newTimeGmt(String time);

    User getUser(String userId) throws UserNotDefinedException;

    User getAnonymousUser();

    String getUserEid(String userId) throws UserNotDefinedException;

    Time newTime();
}
