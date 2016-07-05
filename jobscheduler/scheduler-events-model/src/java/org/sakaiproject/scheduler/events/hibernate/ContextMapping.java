package org.sakaiproject.scheduler.events.hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Just maps a context ID to a quartz trigger UUID.
 */
@Entity
public class ContextMapping {

    @Id
    // This is the ID of the quartz trigger
    private String uuid;

    // This is the context ID (opaque ID) passed when the job was created.
    private String contextId;
}
