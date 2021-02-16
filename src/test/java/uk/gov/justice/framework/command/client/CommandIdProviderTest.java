package uk.gov.justice.framework.command.client;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandIdProviderTest {

    @Mock
    private ToConsolePrinter toConsolePrinter;
    
    @InjectMocks
    private CommandIdProvider commandIdProvider;

    @Test
    public void shouldGetTheCommandIdFromTheCommandLine() throws Exception {

        final UUID commandId = randomUUID();

        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLine.getOptionValue("attach")).thenReturn(commandId.toString());

        final Optional<UUID> commandIdOptional = commandIdProvider.getCommandId(commandLine);

        if (commandIdOptional.isPresent()) {
            assertThat(commandIdOptional.get(), is(commandId));
        } else {
            fail();
        }

        verifyZeroInteractions(toConsolePrinter);
    }

    @Test
    public void shouldLogAndReturnEmptyIfNoCommandIdFound() throws Exception {

        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLine.getOptionValue("attach")).thenReturn(null);

        assertThat(commandIdProvider.getCommandId(commandLine).isPresent(), is(false));

        verify(toConsolePrinter).println("Please specify a command id to attach to");
    }

    @Test
    public void shouldLogAndReturnEmptyIfCommandIdIsNotAUuid() throws Exception {

        final String dodgyCommandId = "something silly";
        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLine.getOptionValue("attach")).thenReturn(dodgyCommandId);

        assertThat(commandIdProvider.getCommandId(commandLine).isPresent(), is(false));

        verify(toConsolePrinter).printf("Command id '%s' is not a UUID", dodgyCommandId);
    }
}
