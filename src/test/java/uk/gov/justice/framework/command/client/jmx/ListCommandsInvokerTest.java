package uk.gov.justice.framework.command.client.jmx;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.Credentials;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListCommandsInvokerTest {

    @Mock
    private SystemCommanderClientFactory systemCommanderClientFactory;

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private ListCommandsInvoker listCommandsInvoker;

    @Test
    public void shouldMakeAJmxCallToRetrieveTheListOfCommands() throws Exception {

        final String contextName = "my-context";
        final String host = "localhost";
        final int port = 92834;

        final List<SystemCommandDetails> systemCommandDetails = singletonList(mock(SystemCommandDetails.class));

        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean commanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(empty());
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(commanderMBean);
        when(commanderMBean.listCommands()).thenReturn(systemCommandDetails);

        assertThat(listCommandsInvoker.listSystemCommands(jmxParameters), is(of(systemCommandDetails)));

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient);

        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(systemCommanderClient).getRemote(contextName);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
    }

    @Test
    public void shouldLogIfUsingCredentials() throws Exception {

        final String contextName = "my-context";
        final String host = "localhost";
        final int port = 92834;
        final String username = "Fred";

        final List<SystemCommandDetails> systemCommandDetails = singletonList(mock(SystemCommandDetails.class));

        final Credentials credentials = mock(Credentials.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean commanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(commanderMBean);
        when(commanderMBean.listCommands()).thenReturn(systemCommandDetails);

        assertThat(listCommandsInvoker.listSystemCommands(jmxParameters), is(of(systemCommandDetails)));

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient);

        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(toConsolePrinter).printf("Connecting with credentials for user '%s'", username);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(systemCommanderClient).getRemote(contextName);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
    }
}
