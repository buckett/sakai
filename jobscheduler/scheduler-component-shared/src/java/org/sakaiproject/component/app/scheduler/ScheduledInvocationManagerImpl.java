package org.sakaiproject.component.app.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.sakaiproject.component.app.scheduler.jobs.ScheduledInvocationJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sakaiproject.api.app.scheduler.DelayedInvocation;
import org.sakaiproject.api.app.scheduler.ScheduledInvocationManager;
import org.sakaiproject.api.app.scheduler.SchedulerManager;
import org.sakaiproject.id.api.IdManager;
import org.sakaiproject.time.api.Time;

public class ScheduledInvocationManagerImpl implements ScheduledInvocationManager {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduledInvocationManagerImpl.class);

	public static final String GROUP_NAME = "org.sakaiproject.component.app.scheduler.jobs.ScheduledInvocationJob";
	
	/** Dependency: IdManager */
	protected IdManager m_idManager = null;

	public void setIdManager(IdManager service) {
		m_idManager = service;
	}


	/** Dependency: SchedulerManager */
	protected SchedulerManager m_schedulerManager = null;

	public void setSchedulerManager(SchedulerManager service) {
		m_schedulerManager = service;
	}

   public void destroy() {
      LOG.info("destroy()");
   }

	/* (non-Javadoc)
     * @see org.sakaiproject.api.app.scheduler.ScheduledInvocationManager#createDelayedInvocation(org.sakaiproject.time.api.Time, java.lang.String, java.lang.String)
     */
	public String createDelayedInvocation(Time time, String componentId, String opaqueContext) {
 		try {
 			String uuid = m_idManager.createUuid();
 			Scheduler scheduler = m_schedulerManager.getScheduler();
			JobKey key = new JobKey(componentId, GROUP_NAME);
 			JobDetail detail = scheduler.getJobDetail(key);
 			if (detail == null) {
				detail = JobBuilder.newJob(ScheduledInvocationJob.class)
						.withIdentity(componentId, GROUP_NAME)
						.storeDurably()
						.build();
 				scheduler.addJob(detail, false);
 			}
			// Non-repeating trigger.
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(uuid, GROUP_NAME)
					.startAt(new Date(time.getTime()))
					.forJob(componentId, GROUP_NAME)
					.usingJobData("contextId", opaqueContext)
					.build();
			scheduler.scheduleJob(trigger);
			LOG.info("Created new Delayed Invocation: uuid=" + uuid);
			return uuid;
		} catch (SchedulerException se) {
			LOG.error("Failed to create new Delayed Invocation: componentId=" + componentId +
					", opaqueContext=" + opaqueContext, se);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.api.app.scheduler.ScheduledInvocationManager#deleteDelayedInvocation(java.lang.String)
	 */
	public void deleteDelayedInvocation(String uuid) {

		LOG.debug("Removing Delayed Invocation: " + uuid);
		try {
			TriggerKey key = new TriggerKey(uuid, GROUP_NAME);
			m_schedulerManager.getScheduler().unscheduleJob(key);
		} catch (SchedulerException e) {
			LOG.error("Failed to remove Delayed Invocation: uuid="+ uuid);
		}

	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.api.app.scheduler.ScheduledInvocationManager#deleteDelayedInvocation(java.lang.String, java.lang.String)
	 */
	public void deleteDelayedInvocation(String componentId, String opaqueContext) {
		LOG.debug("componentId=" + componentId + ", opaqueContext=" + opaqueContext);

		try {
			Scheduler scheduler = m_schedulerManager.getScheduler();
			List<String> jobNames = scheduler.getJobGroupNames();
			for (String jobName : jobNames) {
				if (componentId.length() > 0 && !(jobName.equals(componentId))) {
					// If we're filtering by component Id and it doesn't match skip.
					continue;
				}
				JobKey key = new JobKey(jobName, GROUP_NAME);
				JobDetail detail = scheduler.getJobDetail(key);
				if (detail != null) {
					List<? extends Trigger> triggers = scheduler.getTriggersOfJob(key);
					for (Trigger trigger: triggers) {
						String contextId = trigger.getJobDataMap().getString("contextId");
						if (opaqueContext.length() > 0 && !(opaqueContext.equals(contextId))) {
							// If we're filtering by opaqueContent and it doesn't match skip.
							continue;
						}
						// Unscehdule the trigger.
						scheduler.unscheduleJob(trigger.getKey());
					}
				}
			}
		} catch (SchedulerException se) {
			LOG.error("Failure while attempting to remove invocations matching: componentId=" + componentId + ", opaqueContext=" + opaqueContext, se);
		}
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.api.app.scheduler.ScheduledInvocationManager#findDelayedInvocations(java.lang.String, java.lang.String)
	 */
	public DelayedInvocation[] findDelayedInvocations(String componentId, String opaqueContext) {
		LOG.debug("componentId=" + componentId + ", opaqueContext=" + opaqueContext);
		List<DelayedInvocation> invocations = new ArrayList<DelayedInvocation>();
		try {
			Scheduler scheduler = m_schedulerManager.getScheduler();
			Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(GROUP_NAME));
			for (JobKey jobKey : jobKeys) {
				if (componentId.length() > 0 && !(jobKey.getName().equals(componentId))) {
					// If we're filtering by component Id and it doesn't match skip.
					continue;
				}
				JobDetail detail = scheduler.getJobDetail(jobKey);
				if (detail != null) {
					List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
					for (Trigger trigger: triggers) {
						String contextId = trigger.getJobDataMap().getString("contextId");
						if (opaqueContext.length() > 0 && !(opaqueContext.equals(contextId))) {
							// If we're filtering by opaqueContent and it doesn't match skip.
							continue;
						}
						// Add this one to the list.
						invocations.add(new DelayedInvocation(trigger.getKey().getName(), trigger.getNextFireTime(), jobKey.getName(), contextId));
					}
				}
			}
		} catch (SchedulerException se) {
			LOG.error("Failure while attempting to remove invocations matching: componentId=" + componentId + ", opaqueContext=" + opaqueContext, se);
		}
		return invocations.toArray(new DelayedInvocation[]{});
	}
}
