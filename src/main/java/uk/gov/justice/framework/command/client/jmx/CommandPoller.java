package uk.gov.justice.framework.command.client.jmx;

import static java.lang.String.format;
import static java.time.Duration.between;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.util.Sleeper;
import uk.gov.justice.framework.command.client.util.UtcClock;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.inject.Inject;

public class CommandPoller {

    @Inject
    private CommandChecker commandChecker;

    @Inject
    private UtcClock clock;

    @Inject
    private Sleeper sleeper;

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public void runUntilComplete(final SystemCommanderMBean systemCommanderMBean, final RunContext runContext) {

        final UUID commandId = runContext.getCommandId();
        final String commandName = runContext.getCommandName();
        final ZonedDateTime startTime = runContext.getStartTime();

        int count = 0;
        while (! commandChecker.commandComplete(systemCommanderMBean, commandId, startTime)) {
            sleeper.sleepFor(1_000);
            count++;

            if (count % 10 == 0) {
                final long durationMillis = between(startTime, clock.now()).toMillis();

                final String duration = formatDuration(durationMillis, "HH:mm:ss");

                toConsolePrinter.println(format("%s running for %s (hh:mm:ss)", commandName, duration));
            }
        }
    }
}
