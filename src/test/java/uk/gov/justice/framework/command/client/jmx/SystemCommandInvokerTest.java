package uk.gov.justice.framework.command.client.jmx;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.command.SystemCommand;
import uk.gov.justice.services.jmx.command.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.Credentials;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxAuthenticationException;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.io.IOException;

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
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private SystemCommandInvoker systemCommandInvoker;

    @Test
    public void shouldMakeAJmxCallToRetrieveTheListOfCommands() throws Exception {

        final String host = "localhost";
        final int port = 92834;
        final String commandName = "SOME_COMMAND";

        final SystemCommand systemCommand = mock(SystemCommand.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(empty());
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote()).thenReturn(systemCommanderMBean);
        when(systemCommand.getName()).thenReturn(commandName);

        systemCommandInvoker.runSystemCommand(systemCommand, jmxParameters);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient);

        inOrder.verify(toConsolePrinter).printf("Connecting to Wildfly instance at '%s' on port %d", host, port);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(systemCommanderClient).getRemote();
        inOrder.verify(toConsolePrinter).println("Connected to remote server");
        systemCommanderMBean.call(systemCommand);

        toConsolePrinter.printf("System command '%s' successfully sent", systemCommand.getName());
    }

    @Test
    public void shouldLogIfUsingCredentials() throws Exception {

        final String username = "Fred";
        final String host = "localhost";
        final int port = 92834;
        final String commandName = "SOME_COMMAND";

        final Credentials credentials = mock(Credentials.class);
        final SystemCommand systemCommand = mock(SystemCommand.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote()).thenReturn(systemCommanderMBean);
        when(systemCommand.getName()).thenReturn(commandName);

        systemCommandInvoker.runSystemCommand(systemCommand, jmxParameters);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient,
                systemCommanderMBean);

        inOrder.verify(toConsolePrinter).printf("Connecting to Wildfly instance at '%s' on port %d", host, port);
        inOrder.verify(toConsolePrinter).printf("Connecting with credentials for user '%s'", username);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(systemCommanderClient).getRemote();
        inOrder.verify(toConsolePrinter).println("Connected to remote server");
        inOrder.verify(systemCommanderMBean).call(systemCommand);
        inOrder.verify(toConsolePrinter).printf("System command '%s' successfully sent", systemCommand.getName());
    }

    @Test
    public void shoulLogAndReturnEmptyIfAuthenticationFails() throws Exception {

        final String host = "localhost";
        final int port = 92834;
        final String username = "Fred";

        final JmxAuthenticationException jmxAuthenticationException = new JmxAuthenticationException("Ooops", new IOException());

        final SystemCommand systemCommand = mock(SystemCommand.class);
        final Credentials credentials = mock(Credentials.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);

        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenThrow(jmxAuthenticationException);

        systemCommandInvoker.runSystemCommand(systemCommand, jmxParameters);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient);

        inOrder.verify(toConsolePrinter).printf("Connecting to Wildfly instance at '%s' on port %d", host, port);
        inOrder.verify(toConsolePrinter).printf("Connecting with credentials for user '%s'", username);
        inOrder.verify(toConsolePrinter).println("Authentication failed. Please ensure your username and password are correct");
    }
}
