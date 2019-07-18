package uk.gov.justice.framework.command.client.cdi.producers;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OptionsFactoryTest {

    @InjectMocks
    private OptionsFactory optionsFactory;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateNewOptionsObjectConfiguredWithTheCorrectCommandLineParameters() throws Exception {

        final Options options = optionsFactory.createOptions();

        final Collection<Option> allOptions = options.getOptions();

        assertThat(allOptions.size(), is(8));

        assertThat(allOptions, hasItem(new Option("help", false, "Show help.")));
        assertThat(allOptions, hasItem(new Option("cn", "context-name", true, "The name of the context on which to run the command. Required")));
        assertThat(allOptions, hasItem(new Option("c", "command", true, "Framework command to execute. Run with --list for a list of all commands")));
        assertThat(allOptions, hasItem(new Option("h", "host", true, "Hostname or IP address of the Wildfly server. Defaults to localhost")));
        assertThat(allOptions, hasItem(new Option("p", "port", true, "Wildfly management port. Defaults to 9990")));
        assertThat(allOptions, hasItem(new Option("u", "username", true, "Optional username for Wildfly management security")));
        assertThat(allOptions, hasItem(new Option("pw", "password", true, "Optional password for Wildfly management security")));
        assertThat(allOptions, hasItem(new Option("l", "list", false, "List of all framework commands")));
    }
}
