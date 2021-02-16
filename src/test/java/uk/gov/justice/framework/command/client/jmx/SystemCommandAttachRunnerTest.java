package uk.gov.justice.framework.command.client.jmx;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_COMPLETE;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_FAILED;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_IN_PROGRESS;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_RECEIVED;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.util.UtcClock;
import uk.gov.justice.services.jmx.api.CommandNotFoundException;
import uk.gov.justice.services.jmx.api.domain.SystemCommandStatus;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemCommandAttachRunnerTest {

    @Mock
    private CommandPoller commandPoller;

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private SystemCommandAttachRunner systemCommandAttachRunner;

    @Test
    public void shouldAttachToRunningCommandAndPollIfCommandStateIsInProgress() throws Exception {

        final UUID commandId = randomUUID();
        final String commandName = "SOME_COMMAND";
        final ZonedDateTime startTime = new UtcClock().now();
        final RunContext runContext = new RunContext(commandId, commandName, startTime);

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);
        final SystemCommandStatus commandStatus = mock(SystemCommandStatus.class);

        when(systemCommanderMBean.getCommandStatus(commandId)).thenReturn(commandStatus);
        when(commandStatus.getSystemCommandName()).thenReturn(commandName);
        when(commandStatus.getCommandState()).thenReturn(COMMAND_IN_PROGRESS);
        when(commandStatus.getStatusChangedAt()).thenReturn(startTime);

        systemCommandAttachRunner.attach(commandId, systemCommanderMBean);

        verify(commandPoller).runUntilComplete(systemCommanderMBean, runContext);
        verifyZeroInteractions(toConsolePrinter);
    }

    @Test
    public void shouldAttachToRunningCommandAndPollIfCommandStateIsReceived() throws Exception {

        final UUID commandId = randomUUID();
        final String commandName = "SOME_COMMAND";
        final ZonedDateTime startTime = new UtcClock().now();
        final RunContext runContext = new RunContext(commandId, commandName, startTime);

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);
        final SystemCommandStatus commandStatus = mock(SystemCommandStatus.class);

        when(systemCommanderMBean.getCommandStatus(commandId)).thenReturn(commandStatus);
        when(commandStatus.getSystemCommandName()).thenReturn(commandName);
        when(commandStatus.getCommandState()).thenReturn(COMMAND_RECEIVED);
        when(commandStatus.getStatusChangedAt()).thenReturn(startTime);

        systemCommandAttachRunner.attach(commandId, systemCommanderMBean);

        verify(commandPoller).runUntilComplete(systemCommanderMBean, runContext);
        verifyZeroInteractions(toConsolePrinter);
    }

    @Test
    public void shouldNotAttachToCommandIfCommandStateIsComplete() throws Exception {

        final UUID commandId = randomUUID();
        final String commandName = "SOME_COMMAND";

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);
        final SystemCommandStatus commandStatus = mock(SystemCommandStatus.class);

        when(systemCommanderMBean.getCommandStatus(commandId)).thenReturn(commandStatus);
        when(commandStatus.getSystemCommandName()).thenReturn(commandName);
        when(commandStatus.getCommandState()).thenReturn(COMMAND_COMPLETE);

        systemCommandAttachRunner.attach(commandId, systemCommanderMBean);

        verify(toConsolePrinter).printf("Cannot attach to Command with id '%s'. Command is not running. Current command status: %s", commandId, COMMAND_COMPLETE);
        verifyZeroInteractions(commandPoller);
    }

    @Test
    public void shouldNotAttachToCommandIfCommandStateIsFailed() throws Exception {

        final UUID commandId = randomUUID();
        final String commandName = "SOME_COMMAND";

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);
        final SystemCommandStatus commandStatus = mock(SystemCommandStatus.class);

        when(systemCommanderMBean.getCommandStatus(commandId)).thenReturn(commandStatus);
        when(commandStatus.getSystemCommandName()).thenReturn(commandName);
        when(commandStatus.getCommandState()).thenReturn(COMMAND_FAILED);

        systemCommandAttachRunner.attach(commandId, systemCommanderMBean);

        verify(toConsolePrinter).printf("Cannot attach to Command with id '%s'. Command is not running. Current command status: %s", commandId, COMMAND_FAILED);
        verifyZeroInteractions(commandPoller);
    }

    @Test
    public void shouldNotAttachIfNoCommandFound() throws Exception {

        final UUID commandId = randomUUID();
        final String commandName = "SOME_COMMAND";
        final ZonedDateTime startTime = new UtcClock().now();
        final RunContext runContext = new RunContext(commandId, commandName, startTime);

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);
        final SystemCommandStatus commandStatus = mock(SystemCommandStatus.class);

        when(systemCommanderMBean.getCommandStatus(commandId)).thenReturn(commandStatus);
        when(commandStatus.getSystemCommandName()).thenReturn(commandName);
        when(commandStatus.getCommandState()).thenReturn(COMMAND_IN_PROGRESS);
        when(commandStatus.getStatusChangedAt()).thenReturn(startTime);

        systemCommandAttachRunner.attach(commandId, systemCommanderMBean);

        verify(commandPoller).runUntilComplete(systemCommanderMBean, runContext);
        verifyZeroInteractions(toConsolePrinter);
    }

    @Test
    public void shouldPrintAndDoNothingIfCommandNotFound() throws Exception {

        final UUID commandId = randomUUID();
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(systemCommanderMBean.getCommandStatus(commandId)).thenThrow(new CommandNotFoundException("Oops"));

        systemCommandAttachRunner.attach(commandId, systemCommanderMBean);

        verify(toConsolePrinter).printf("No system command exists with id '%s", commandId);
        verifyZeroInteractions(commandPoller);
    }
}
