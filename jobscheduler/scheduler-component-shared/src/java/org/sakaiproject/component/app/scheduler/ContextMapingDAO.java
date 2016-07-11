package org.sakaiproject.component.app.scheduler;

import org.hibernate.SessionFactory;
import org.sakaiproject.scheduler.events.hibernate.ContextMapping;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by buckett on 08/07/2016.
 */
public class ContextMapingDAO {

    SessionFactory sessionFactory;

    @Transactional
    public ContextMapping find(String uuid) {
        ContextMapping o = sessionFactory.getCurrentSession().get(ContextMapping.class, uuid);

    }

}
