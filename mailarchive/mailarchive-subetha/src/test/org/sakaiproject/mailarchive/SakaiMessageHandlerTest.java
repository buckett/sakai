package org.sakaiproject.mailarchive;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.sakaiproject.alias.api.AliasService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.mailarchive.api.MailArchiveService;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.thread_local.api.ThreadLocalManager;
import org.sakaiproject.time.api.TimeService;
import org.sakaiproject.user.api.UserDirectoryService;
import org.subethamail.smtp.client.SMTPException;
import org.subethamail.smtp.client.SmartClient;
import org.subethamail.smtp.server.SMTPServer;

import java.io.IOException;

/**
 * This is designed to test the basic handling on mail.
 */
@RunWith(MockitoJUnitRunner.class)
public class SakaiMessageHandlerTest {

    @Mock
    private ServerConfigurationService serverConfigurationService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private AliasService aliasService;

    @Mock
    private UserDirectoryService userDirectoryService;

    @Mock
    private SiteService siteService;

    @Mock
    private TimeService timeService;

    @Mock
    private ThreadLocalManager threadLocalManager;

    @Mock
    private ContentHostingService contentHostingService;

    @Mock
    private MailArchiveService mailArchiveService;

    private SakaiMessageHandlerFactory messageHandlerFactory;

    @Before
    public void setUp() throws Exception {
        messageHandlerFactory = new SakaiMessageHandlerFactory();
        messageHandlerFactory.setServerConfigurationService(serverConfigurationService);
        messageHandlerFactory.setEntityManager(entityManager);
        messageHandlerFactory.setAliasService(aliasService);
        messageHandlerFactory.setUserDirectoryService(userDirectoryService);
        messageHandlerFactory.setSiteService(siteService);
        messageHandlerFactory.setTimeService(timeService);
        messageHandlerFactory.setThreadLocalManager(threadLocalManager);
        messageHandlerFactory.setContentHostingService(contentHostingService);
        messageHandlerFactory.setMailArchiveService(mailArchiveService);

        // Binding to port 0 means that it picks a random port to listen on.
        Mockito.when(serverConfigurationService.getInt("smtp.port", 25)).thenReturn(0);
        Mockito.when(serverConfigurationService.getBoolean("smtp.enabled", false)).thenReturn(true);
        Mockito.when(serverConfigurationService.getString("sakai.version", "unknown")).thenReturn("1.2.3");
        Mockito.when(serverConfigurationService.getServerName()).thenReturn("sakai.example.com");

        messageHandlerFactory.init();
    }

    @After
    public void tearDown() throws Exception {
        messageHandlerFactory.destroy();
    }


    @Test
    public void testStarted() {
        // This just checks that the server got started.
        Assert.assertNotNull("No server started up.", messageHandlerFactory.getServer());
    }

    @Test(expected = SMTPException.class)
    public void testRejectedDomain() throws IOException {
        SmartClient client = createClient();
        client.from("sender@example.com");
        client.to("test@gmail.com");
    }

    @Test
    public void testRejectedAddress() throws Exception {
        Mockito.when(aliasService.getTarget("user")).thenThrow(IdUnusedException.class);
        SmartClient client = createClient();
        client.from("sender@example.com");
        client.to("user@sakai.example.com");
    }

    /**
     * Just creates a client connected to the test server.
     * @return A connected client.
     * @throws IOException If it failed to connecto to the server.
     */
    public SmartClient createClient() throws IOException {
        SMTPServer server = messageHandlerFactory.getServer();
        return new SmartClient("localhost", server.getPort(), "test");
    }


}
