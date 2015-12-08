package org.sakaiproject.announcement.tool;

import org.junit.Test;
import org.mockito.Mockito;
import org.sakaiproject.announcement.api.AnnouncementMessage;
import org.sakaiproject.announcement.api.AnnouncementService;
import org.sakaiproject.entity.api.EntityPropertyNotDefinedException;
import org.sakaiproject.entity.api.EntityPropertyTypeException;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.impl.MyTime;
import org.sakaiproject.util.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnouncementComparatorTest {

    @Test
    public void testTimeSort() throws Exception {
        AnnouncementComparator comparator = new AnnouncementComparator(AnnouncementAction.SORT_DATE, true);

        AnnouncementMessage m1 = Mockito.mock(AnnouncementMessage.class);
        ResourceProperties p1 = Mockito.mock(ResourceProperties.class);
        Mockito.when(m1.getProperties()).thenReturn(p1);
        Time t1 = new MyTime(null, new Date().getTime());
        Mockito.when(p1.getTimeProperty(AnnouncementService.MOD_DATE)).thenReturn(t1);

        List<AnnouncementMessage> messages = new ArrayList<>();





    }
}
