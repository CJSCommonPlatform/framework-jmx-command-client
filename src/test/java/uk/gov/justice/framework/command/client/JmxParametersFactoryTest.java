package uk.gov.justice.framework.command.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.jmx.system.command.client.connection.Credentials;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JmxParametersFactoryTest {

    @InjectMocks
    private JmxParametersFactory jmxParametersFactory;

    @Test
    public void shouldCreateJmxParametersFromTheHostAndPort() throws Exception {

        final String contextName = "my-context";
        final String host = "host";
        final String port = "9990";

        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLine.hasOption("context-name")).thenReturn(true);
        when(commandLine.getOptionValue("context-name")).thenReturn(contextName);
        when(commandLine.getOptionValue("host", "localhost")).thenReturn(host);
        when(commandLine.getOptionValue("port", "9990")).thenReturn(port);
        when(commandLine.hasOption("username")).thenReturn(false);

        final JmxParameters jmxParameters = jmxParametersFactory.createFrom(commandLine);

        assertThat(jmxParameters.getContextName(), is(contextName));
        assertThat(jmxParameters.getHost(), is(host));
        assertThat(jmxParameters.getPort(), is(9990));
        assertThat(jmxParameters.getCredentials().isPresent(), is(false));
    }

    @Test
    public void shouldAlsoAddCredentialsIfTheUsernameExists() throws Exception {

        final String contextName = "my-context";
        final String host = "host";
        final String port = "9990";
        final String username = "Fred";
        final String password = "Password123";

        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLine.hasOption("context-name")).thenReturn(true);
        when(commandLine.getOptionValue("context-name")).thenReturn(contextName);
        when(commandLine.getOptionValue("host", "localhost")).thenReturn(host);
        when(commandLine.getOptionValue("port", "9990")).thenReturn(port);
        when(commandLine.hasOption("username")).thenReturn(true);
        when(commandLine.getOptionValue("username")).thenReturn(username);
        when(commandLine.getOptionValue("password")).thenReturn(password);

        final JmxParameters jmxParameters = jmxParametersFactory.createFrom(commandLine);

        assertThat(jmxParameters.getHost(), is(host));
        assertThat(jmxParameters.getPort(), is(9990));

        final Optional<Credentials> credentials = jmxParameters.getCredentials();

        if (credentials.isPresent()) {
            assertThat(credentials.get().getUsername(), is(username));
            assertThat(credentials.get().getPassword(), is(password));
        } else {
            fail();
        }
    }

    @Test
    public void shouldThrowExceptionIfTheContextNameIsMissing() throws Exception {

        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLine.hasOption("context-name")).thenReturn(false);

        try {
            jmxParametersFactory.createFrom(commandLine);
            fail();
        } catch (final CommandLineException expected) {
            assertThat(expected.getMessage(), is("No context name provided. Please run using --context-name <name> or -cn <name> ."));
        }
    }

    @Test
    public void shouldThrowExceptionIfThePortIsNotANumber() throws Exception {

        final String contextName = "my-context";
        final String host = "host";
        final String port = "not-a-number";

        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLine.hasOption("context-name")).thenReturn(true);
        when(commandLine.getOptionValue("context-name")).thenReturn(contextName);
        when(commandLine.getOptionValue("host", "localhost")).thenReturn(host);
        when(commandLine.getOptionValue("port", "9990")).thenReturn(port);

        try {
            jmxParametersFactory.createFrom(commandLine);
            fail();
        } catch (final CommandLineException expected) {
            assertThat(expected.getMessage(), is("Port number 'not-a-number' is not a number"));
        }
    }
}
