package uk.gov.justice.framework.command.client.jmx;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_COMPLETE;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_FAILED;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_IN_PROGRESS;

import uk.gov.justice.framework.command.client.SystemCommandFailedException;
import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.util.UtcClock;
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
public class CommandCheckerTest {

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @Mock
    private UtcClock clock;

    @InjectMocks
    private CommandChecker commandChecker;

    @Test
    public void shouldLogAndReturnTrueIfTheCommandIsComplete() throws Exception {

        final UUID commandId = randomUUID();
        final String message = "Woo hoo it worked";

        final ZonedDateTime startTime = new UtcClock().now();
        final ZonedDateTime endedAt = startTime.plusMinutes(83);

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);
        final SystemCommandStatus systemCommandStatus = mock(SystemCommandStatus.class);

        when(clock.now()).thenReturn(endedAt);
        when(systemCommanderMBean.getCommandStatus(commandId)).thenReturn(systemCommandStatus);
        when(systemCommandStatus.getCommandState()).thenReturn(COMMAND_COMPLETE);
        when(systemCommandStatus.getSystemCommandName()).thenReturn("CATCHUP");
        when(systemCommandStatus.getMessage()).thenReturn(message);

        assertThat(commandChecker.commandComplete(systemCommanderMBean, commandId, startTime), is(true));

        verify(toConsolePrinter).println(message);
        verify(toConsolePrinter).println("Command CATCHUP complete");
        verify(toConsolePrinter).println("CATCHUP duration 01:23:00 (hours:minutes:seconds)");
    }

    @Test
    public void shouldLogAndThrowExceptionIfTheCommandFails() throws Exception {

        final UUID commandId = randomUUID();

        final String errorMessage = "CATCHUP failed with 23 errors";

        final ZonedDateTime startTime = new UtcClock().now();
        final ZonedDateTime endedAt = startTime.plusMinutes(83);

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);
        final SystemCommandStatus systemCommandStatus = mock(SystemCommandStatus.class);

        when(clock.now()).thenReturn(endedAt);
        when(systemCommanderMBean.getCommandStatus(commandId)).thenReturn(systemCommandStatus);
        when(systemCommandStatus.getCommandState()).thenReturn(COMMAND_FAILED);
        when(systemCommandStatus.getSystemCommandName()).thenReturn("CATCHUP");
        when(systemCommandStatus.getMessage()).thenReturn(errorMessage);

        try {
            commandChecker.commandComplete(systemCommanderMBean, commandId, startTime);
            fail();
        } catch (final SystemCommandFailedException expected) {
            assertThat(expected.getMessage(), is("Comand CATCHUP failed. CATCHUP failed with 23 errors"));
        }

        verify(toConsolePrinter).println("ERROR: Command CATCHUP failed");
        verify(toConsolePrinter).println("ERROR: CATCHUP failed with 23 errors");
        verify(toConsolePrinter).println("CATCHUP duration 01:23:00 (hours:minutes:seconds)");
    }

    @Test
    public void shouldReturnFalseIfTheCommandIsNeitherCompleteNorFailed() throws Exception {

        final UUID commandId = randomUUID();

        final ZonedDateTime startTime = new UtcClock().now();

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);
        final SystemCommandStatus systemCommandStatus = mock(SystemCommandStatus.class);

        when(systemCommanderMBean.getCommandStatus(commandId)).thenReturn(systemCommandStatus);
        when(systemCommandStatus.getCommandState()).thenReturn(COMMAND_IN_PROGRESS);

        assertThat(commandChecker.commandComplete(systemCommanderMBean, commandId, startTime), is(false));
    }
}
