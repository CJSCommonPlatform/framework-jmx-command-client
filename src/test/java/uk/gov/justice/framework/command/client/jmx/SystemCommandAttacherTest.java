package uk.gov.justice.framework.command.client.jmx;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemCommandAttacherTest {

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @Mock
    private SystemCommanderClientFactory systemCommanderClientFactory;

    @Mock
    private SystemCommandAttachRunner systemCommandAttachRunner;

    @InjectMocks
    private SystemCommandAttacher systemCommandAttacher;

    @Test
    public void shouldAttachToRunningSystemCommand() throws Exception {

        final String contextName = "my-context";
        final String host = "localhost";
        final int port = 92834;
        final UUID commandId = randomUUID();

        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(empty());
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);

        systemCommandAttacher.attachToRunningCommand(commandId, jmxParameters);

        verify(toConsolePrinter).printf("Attaching to system command with id'%s'", commandId);
        verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        verify(systemCommandAttachRunner).attach(commandId, systemCommanderMBean);
    }

    @Test
    public void shouldThrowExceptionIfAttachingToSystemCommandFails() throws Exception {

        final String stackTrace = "stack trace";
        final String errorMessage = "Ooops";

        final SystemCommandInvocationFailedException systemCommandInvocationFailedException = new SystemCommandInvocationFailedException(errorMessage, stackTrace);

        final String contextName = "my-context";
        final String host = "localhost";
        final int port = 7239;
        final UUID commandId = randomUUID();

        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(empty());
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        doThrow(systemCommandInvocationFailedException).when(systemCommandAttachRunner).attach(commandId, systemCommanderMBean);

        try {
            systemCommandAttacher.attachToRunningCommand(commandId, jmxParameters);
        } catch (final SystemCommandInvocationFailedException expected) {
            assertThat(expected, is(sameInstance(systemCommandInvocationFailedException)));
        }

        verify(toConsolePrinter).printf("Attaching to system command with id'%s'", commandId);
        verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        verify(toConsolePrinter).printf("The command with id '%s' failed: %s", commandId, errorMessage);
        verify(toConsolePrinter).println(stackTrace);
    }
}
