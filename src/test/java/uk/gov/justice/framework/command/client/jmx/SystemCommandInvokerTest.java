package uk.gov.justice.framework.command.client.jmx;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.api.UnrunnableSystemCommandException;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.Credentials;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemCommandInvokerTest {

    @Mock
    private SystemCommanderClientFactory systemCommanderClientFactory;

    @Mock
    private CommandPoller commandPoller;

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private SystemCommandInvoker systemCommandInvoker;

    @Test
    public void shouldMakeJmxCallToRetrieveTheListOfCommands() throws Exception {

        final String contextName = "my-context";
        final String host = "localhost";
        final int port = 92834;
        final String commandName = "SOME_COMMAND";
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
        when(systemCommanderMBean.call(commandName)).thenReturn(commandId);

        systemCommandInvoker.runSystemCommand(commandName, jmxParameters);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient,
                systemCommanderMBean,
                commandPoller);

        inOrder.verify(toConsolePrinter).printf("Running system command '%s'", commandName);
        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(systemCommanderClient).getRemote(contextName);
        inOrder.verify(systemCommanderMBean).call(commandName);
        inOrder.verify(toConsolePrinter).printf("System command '%s' with id '%s' successfully sent to %s", commandName, commandId, contextName);
        inOrder.verify(commandPoller).runUntilComplete(systemCommanderMBean, commandId, commandName);
    }

    @Test
    public void shouldLogIfUsingCredentials() throws Exception {

        final String contextName = "my-context";
        final String username = "Fred";
        final String host = "localhost";
        final int port = 92834;
        final String commandName = "SOME_COMMAND";
        final UUID commandId = randomUUID();

        final Credentials credentials = mock(Credentials.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        when(systemCommanderMBean.call(commandName)).thenReturn(commandId);

        systemCommandInvoker.runSystemCommand(commandName, jmxParameters);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient,
                systemCommanderMBean,
                commandPoller);

        inOrder.verify(toConsolePrinter).printf("Running system command '%s'", commandName);
        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(toConsolePrinter).printf("Connecting with credentials for user '%s'", username);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(systemCommanderClient).getRemote(contextName);
        inOrder.verify(systemCommanderMBean).call(commandName);
        inOrder.verify(toConsolePrinter).printf("System command '%s' with id '%s' successfully sent to %s", commandName, commandId, contextName);
        inOrder.verify(commandPoller).runUntilComplete(systemCommanderMBean, commandId, commandName);
    }

    @Test(expected = UnrunnableSystemCommandException.class)
    public void shouldLogIfTheCommandIsUnsupported() throws Exception {

        final String contextName = "secret";
        final String host = "localhost";
        final int port = 92834;
        final String username = "Fred";
        final String commandName = "PING";

        final UnrunnableSystemCommandException unrunnableSystemCommandException = new UnrunnableSystemCommandException("Ooops");

        final Credentials credentials = mock(Credentials.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        doThrow(unrunnableSystemCommandException).when(systemCommanderMBean).call(commandName);

        systemCommandInvoker.runSystemCommand(commandName, jmxParameters);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient);

        inOrder.verify(toConsolePrinter).printf("Running system command '%s'", commandName);
        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(toConsolePrinter).printf("The command '%s' is not supported on this %s context", commandName, contextName);
    }

    @Test(expected = SystemCommandInvocationFailedException.class)
    public void shouldLogAndPrintTheServerStackTraceIfTheCommandFails() throws Exception {

        final String contextName = "secret";
        final String host = "localhost";
        final int port = 92834;
        final String username = "Fred";
        final String serverStackTrace = "the stack trace from the server";
        final String errorMessage = "Ooops";
        final String commandName = "PING";

        final SystemCommandInvocationFailedException systemCommandInvocationFailedException = new SystemCommandInvocationFailedException(errorMessage, serverStackTrace);

        final Credentials credentials = mock(Credentials.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        doThrow(systemCommandInvocationFailedException).when(systemCommanderMBean).call(commandName);

        systemCommandInvoker.runSystemCommand(commandName, jmxParameters);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient);

        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(toConsolePrinter).printf("Connecting with credentials for user '%s'", username);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(toConsolePrinter).printf("The command '%s' failed: %s", errorMessage, commandName);
        inOrder.verify(toConsolePrinter).println(serverStackTrace);
    }
}
