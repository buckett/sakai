package org.sakaiproject.calendar.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sakaiproject.calendar.api.CalendarService;
import org.sakaiproject.time.api.TimeRange;
import org.sakaiproject.time.api.TimeService;
import org.sakaiproject.time.api.UserTimeService;
import org.sakaiproject.time.impl.BasicTimeService;
import org.sakaiproject.time.impl.UserLocaleServiceImpl;
import org.sakaiproject.util.ResourceLoader;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PDFExportServiceTest {

    private TimeService timeService;
    private PDFExportService pdfExportService;
    private DocumentBuilder docBuilder;

    @Mock
    private UserLocaleServiceImpl userLocaleService;
    @Mock
    private UserTimeService userTimeService;
    @Mock
    private ResourceLoader resourceLoader;

    @Before
    public void setUp() throws ParserConfigurationException {
        BasicTimeService timeService = new BasicTimeService();
        timeService.setUserTimeService(userTimeService);
        timeService.setUserLocaleService(userLocaleService);
        timeService.init();
        this.timeService = timeService;


        pdfExportService = new PDFExportService(timeService, resourceLoader);
        docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    @Test
    public void testSimple() {
        Document doc = docBuilder.newDocument();
//        TimeRange range = timeService.newTimeRange();
//        pdfExportService.generateXMLDocument(CalendarService.LIST_VIEW, );



    }


}
