package uk.gov.justice.framework.command.client;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.jmx.command.SystemCommand;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandLocatorTest {


    @InjectMocks
    private CommandLocator commandLocator;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldFindTheCorrectCommandByName() throws Exception {

        final String commandName_1 = "commandName_1";
        final String commandName_2 = "commandName_2";
        final String commandName_3 = "commandName_3";
        final String commandName_4 = "commandName_4";

        final SystemCommand systemCommand_1 = mock(SystemCommand.class);
        final SystemCommand systemCommand_2 = mock(SystemCommand.class);
        final SystemCommand systemCommand_3 = mock(SystemCommand.class);
        final SystemCommand systemCommand_4 = mock(SystemCommand.class);

        final List<SystemCommand> systemCommands = asList(systemCommand_1, systemCommand_2, systemCommand_3, systemCommand_4);

        when(systemCommand_1.getName()).thenReturn(commandName_1);
        when(systemCommand_2.getName()).thenReturn(commandName_2);
        when(systemCommand_3.getName()).thenReturn(commandName_3);
        when(systemCommand_4.getName()).thenReturn(commandName_4);


        assertThat(commandLocator.lookupCommand(commandName_1, systemCommands).get(), is(systemCommand_1));
        assertThat(commandLocator.lookupCommand(commandName_2, systemCommands).get(), is(systemCommand_2));
        assertThat(commandLocator.lookupCommand(commandName_3, systemCommands).get(), is(systemCommand_3));
        assertThat(commandLocator.lookupCommand(commandName_4, systemCommands).get(), is(systemCommand_4));
    }

    @Test
    public void shouldReturnEmptyIfTheCommandIsNotFound() throws Exception {

        final String commandName_1 = "commandName_1";
        final String commandName_2 = "commandName_2";
        final String commandName_3 = "commandName_3";
        final String commandName_4 = "commandName_4";

        final SystemCommand systemCommand_1 = mock(SystemCommand.class);
        final SystemCommand systemCommand_2 = mock(SystemCommand.class);
        final SystemCommand systemCommand_3 = mock(SystemCommand.class);
        final SystemCommand systemCommand_4 = mock(SystemCommand.class);

        final List<SystemCommand> systemCommands = asList(systemCommand_1, systemCommand_2, systemCommand_3, systemCommand_4);

        when(systemCommand_1.getName()).thenReturn(commandName_1);
        when(systemCommand_2.getName()).thenReturn(commandName_2);
        when(systemCommand_3.getName()).thenReturn(commandName_3);
        when(systemCommand_4.getName()).thenReturn(commandName_4);


        assertThat(commandLocator.lookupCommand("some other command name", systemCommands).isPresent(), is(false));
    }
}
