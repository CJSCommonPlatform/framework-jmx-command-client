package uk.gov.justice.framework.command.client.jmx;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.util.Sleeper;
import uk.gov.justice.framework.command.client.util.UtcClock;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandPollerTest {

    @Mock
    private CommandChecker commandChecker;

    @Mock
    private UtcClock clock;

    @Mock
    private Sleeper sleeper;

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private CommandPoller commandPoller;

    @Test
    public void shouldCheckCommandUntilComplete() throws Exception {

        final UUID commandId = UUID.randomUUID();
        final String commandName = "CATCHUP";

        final ZonedDateTime startTime = new UtcClock().now();

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(clock.now()).thenReturn(startTime);
        when(commandChecker.commandComplete(systemCommanderMBean, commandId, startTime)).thenReturn(false, false, true);

        commandPoller.runUntilComplete(systemCommanderMBean, commandId, commandName);

        verify(sleeper, times(2)).sleepFor(1_000);
        verifyZeroInteractions(toConsolePrinter);
    }

    @Test
    public void shouldLogProgressEveryTenthCall() throws Exception {

        final UUID commandId = UUID.randomUUID();
        final String commandName = "CATCHUP";

        final ZonedDateTime startTime = new UtcClock().now();
        final ZonedDateTime now = startTime.plusSeconds(10);

        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(clock.now()).thenReturn(startTime, now);
        when(commandChecker.commandComplete(systemCommanderMBean, commandId, startTime)).thenReturn(false, false, false, false, false, false, false, false, false, false, true);

        commandPoller.runUntilComplete(systemCommanderMBean, commandId, commandName);

        verify(sleeper, times(10)).sleepFor(1_000);
        verify(toConsolePrinter).println("CATCHUP running for 10 seconds");
    }
}
