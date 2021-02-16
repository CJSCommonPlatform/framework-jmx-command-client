package uk.gov.justice.framework.command.client;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.io.CommandPrinter;
import uk.gov.justice.framework.command.client.jmx.SystemCommandAttacher;
import uk.gov.justice.framework.command.client.jmx.SystemCommandInvoker;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandExecutorTest {

    @Mock
    private SystemCommandInvoker systemCommandInvoker;

    @Mock
    private SystemCommandAttacher systemCommandAttacher;

    @Mock
    private CommandPrinter commandPrinter;

    @InjectMocks
    private CommandExecutor commandExecutor;

    @Test
    public void shouldLookupSystemCommandByNameAndExecute() throws Exception {

        final String commandName = "CATCHUP";

        final SystemCommandDetails systemCommandDetails_1 = mock(SystemCommandDetails.class);
        final SystemCommandDetails systemCommandDetails_2 = mock(SystemCommandDetails.class);

        final CommandLine commandLine = mock(CommandLine.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final List<SystemCommandDetails> systemCommands = asList(systemCommandDetails_1, systemCommandDetails_2);

        when(commandLine.hasOption("list")).thenReturn(false);
        when(commandLine.hasOption("attach")).thenReturn(false);
        when(commandLine.getOptionValue("command")).thenReturn(commandName);

        commandExecutor.executeCommand(commandLine, jmxParameters, systemCommands);

        verify(systemCommandInvoker).runSystemCommand(commandName, jmxParameters);
    }

    @Test
    public void shouldAttachToCurrentlyRunningCommand() throws Exception {

        final UUID commandId = randomUUID();

        final SystemCommandDetails systemCommandDetails_1 = mock(SystemCommandDetails.class);
        final SystemCommandDetails systemCommandDetails_2 = mock(SystemCommandDetails.class);

        final CommandLine commandLine = mock(CommandLine.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final List<SystemCommandDetails> systemCommands = asList(systemCommandDetails_1, systemCommandDetails_2);

        when(commandLine.hasOption("list")).thenReturn(false);
        when(commandLine.hasOption("attach")).thenReturn(true);
        when(commandLine.getOptionValue("attach")).thenReturn(commandId.toString());

        commandExecutor.executeCommand(commandLine, jmxParameters, systemCommands);

        verify(systemCommandAttacher).attachToRunningCommand(commandId, jmxParameters);
    }

    @Test
    public void shouldListSystemCommandsIfCommandLineOptionIsList() throws Exception {

        final SystemCommandDetails systemCommandDetails_1 = mock(SystemCommandDetails.class);
        final SystemCommandDetails systemCommandDetails_2 = mock(SystemCommandDetails.class);

        final CommandLine commandLine = mock(CommandLine.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final List<SystemCommandDetails> systemCommands = asList(systemCommandDetails_1, systemCommandDetails_2);

        when(commandLine.hasOption("list")).thenReturn(true);

        commandExecutor.executeCommand(commandLine, jmxParameters, systemCommands);

        verify(commandPrinter).printSystemCommands(systemCommands);
        verifyZeroInteractions(systemCommandInvoker);
    }
}
