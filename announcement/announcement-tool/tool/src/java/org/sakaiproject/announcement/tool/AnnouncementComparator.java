package org.sakaiproject.announcement.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.announcement.api.AnnouncementMessage;
import org.sakaiproject.announcement.cover.AnnouncementService;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.time.api.Time;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Comparator;

/**
 * A Comparator for sorting announcements.
 */
class AnnouncementComparator implements Comparator<AnnouncementMessage> {
    // the criteria
    private String m_criteria = null;

    private RuleBasedCollator collator_ini = (RuleBasedCollator) Collator.getInstance();
    private Collator collator = Collator.getInstance();

    private static final Log M_log = LogFactory.getLog(AnnouncementComparator.class);

    {
        try {
            collator = new RuleBasedCollator(collator_ini.getRules().replaceAll("<'\u005f'", "<' '<'\u005f'"));
            ;
        } catch (ParseException e) {
            M_log.error(this + " Cannot init RuleBasedCollator. Will use the default Collator instead.", e);
        }
    }

    // the criteria - asc
    boolean m_asc = true;

    /**
     * constructor
     *
     * @param criteria The sort criteria string
     * @param asc      The sort order string. "true" if ascending; "false" otherwise.
     */
    public AnnouncementComparator(String criteria, boolean asc) {
        m_criteria = criteria;
        m_asc = asc;

    } // constructor

    /**
     * implementing the compare function
     *
     * @param a1 The first object
     * @param a2 The second object
     * @return The compare result. 1 is a1 < a2; -1 otherwise
     */
    public int compare(AnnouncementMessage a1, AnnouncementMessage a2) {
        int result = -1;

        if (m_criteria.equals(AnnouncementAction.SORT_SUBJECT)) {
            // sorted by the discussion message subject
            result = collator.compare(((AnnouncementMessage) a1).getAnnouncementHeader().getSubject(),
                    ((AnnouncementMessage) a2).getAnnouncementHeader().getSubject());

        } else if (m_criteria.equals(AnnouncementAction.SORT_DATE)) {

            Time o1ModDate = null;
            Time o2ModDate = null;

            try {
                o1ModDate = ((AnnouncementMessage) a1).getProperties().getTimeProperty(AnnouncementService.MOD_DATE);
            } catch (Exception e) {
                // release date not set, use the date in header
                // NOTE: this is an edge use case for courses with pre-existing announcements that do not yet have MOD_DATE
                o1ModDate = ((AnnouncementMessage) a1).getHeader().getDate();
            }

            try {
                o2ModDate = ((AnnouncementMessage) a2).getProperties().getTimeProperty(AnnouncementService.MOD_DATE);
            } catch (Exception e) {
                // release date not set, use the date in the header
                // NOTE: this is an edge use case for courses with pre-existing announcements that do not yet have MOD_DATE
                o2ModDate = ((AnnouncementMessage) a2).getHeader().getDate();
            }

            if (o1ModDate != null && o2ModDate != null) {
                // sorted by the discussion message date
                result = o1ModDate.compareTo(o2ModDate);
            } else if (o1ModDate == null) {
                return 1;
            } else if (o2ModDate == null) {
                return -1;
            } else {
                return 0;
            }
        } else if (m_criteria.equals(AnnouncementAction.SORT_MESSAGE_ORDER)) {
            int order1 = ((AnnouncementMessage) a1).getAnnouncementHeader().getMessage_order();
            int order2 = ((AnnouncementMessage) a2).getAnnouncementHeader().getMessage_order();
            // sorted by the message order
            if (order1 < order2) {
                result = -1;
            } else if (order1 > order2) {
                result = 1;
            } else {
                return 0;
            }
        } else if (m_criteria.equals(AnnouncementAction.SORT_RELEASEDATE)) {
            Time o1releaseDate = null;
            Time o2releaseDate = null;

            try {
                o1releaseDate = ((AnnouncementMessage) a1).getProperties().getTimeProperty(AnnouncementService.RELEASE_DATE);
            } catch (Exception e) {
                // release date not set, go on
            }

            try {
                o2releaseDate = ((AnnouncementMessage) a2).getProperties().getTimeProperty(AnnouncementService.RELEASE_DATE);
            } catch (Exception e) {
                // release date not set, go on
            }

            if (o1releaseDate != null && o2releaseDate != null) {
                result = o1releaseDate.compareTo(o2releaseDate);
            } else if (o1releaseDate == null) {
                return 1;
            } else if (o2releaseDate == null) {
                return -1;
            } else {
                return 0;
            }
        } else if (m_criteria.equals(AnnouncementAction.SORT_RETRACTDATE)) {
            Time o1retractDate = null;
            Time o2retractDate = null;

            try {
                o1retractDate = ((AnnouncementMessage) a1).getProperties().getTimeProperty(AnnouncementService.RETRACT_DATE);
            } catch (Exception e) {
                // release date not set, go on
            }

            try {
                o2retractDate = ((AnnouncementMessage) a2).getProperties().getTimeProperty(AnnouncementService.RETRACT_DATE);
            } catch (Exception e) {
                // release date not set, go on
            }

            if (o1retractDate != null && o2retractDate != null) {
                result = o1retractDate.compareTo(o2retractDate);
            } else if (o1retractDate == null) {
                return 1;
            } else if (o2retractDate == null) {
                return -1;
            } else {
                return 0;
            }
        } else if (m_criteria.equals(AnnouncementAction.SORT_FROM)) {
            // sorted by the discussion message subject
            result = collator.compare(((AnnouncementMessage) a1).getAnnouncementHeader().getFrom().getSortName(),
                    ((AnnouncementMessage) a2).getAnnouncementHeader().getFrom().getSortName());
        } else if (m_criteria.equals(AnnouncementAction.SORT_CHANNEL)) {
            // sorted by the channel name.
            result = collator.compare(((AnnouncementAction.AnnouncementWrapper) a1).getChannelDisplayName(),
                    ((AnnouncementAction.AnnouncementWrapper) a2).getChannelDisplayName());
        } else if (m_criteria.equals(AnnouncementAction.SORT_PUBLIC)) {
            // sorted by the public view attribute
            String factor1 = ((AnnouncementMessage) a1).getProperties().getProperty(ResourceProperties.PROP_PUBVIEW);
            if (factor1 == null) factor1 = "false";
            String factor2 = ((AnnouncementMessage) a2).getProperties().getProperty(ResourceProperties.PROP_PUBVIEW);
            if (factor2 == null) factor2 = "false";
            result = collator.compare(factor1, factor2);
        } else if (m_criteria.equals(AnnouncementAction.SORT_FOR)) {
            // sorted by the public view attribute
            String factor1 = ((AnnouncementAction.AnnouncementWrapper) a1).getRange();
            String factor2 = ((AnnouncementAction.AnnouncementWrapper) a2).getRange();
            result = collator.compare(factor1, factor2);
        } else if (m_criteria.equals(AnnouncementAction.SORT_GROUPTITLE)) {
            // sorted by the group title
            String factor1 = ((Group) a1).getTitle();
            String factor2 = ((Group) a2).getTitle();
            result = collator.compare(factor1, factor2);
        } else if (m_criteria.equals(AnnouncementAction.SORT_GROUPDESCRIPTION)) {
            // sorted by the group title
            String factor1 = ((Group) a1).getDescription();
            String factor2 = ((Group) a2).getDescription();
            if (factor1 == null) {
                factor1 = "";
            }
            if (factor2 == null) {
                factor2 = "";
            }
            result = collator.compare(factor1, factor2);
        }

        // sort ascending or descending
        if (!m_asc) {
            result = -result;
        }
        return result;

    } // compare

} // AnnouncementComparator
