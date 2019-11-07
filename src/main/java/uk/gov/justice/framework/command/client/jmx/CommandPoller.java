package uk.gov.justice.framework.command.client.jmx;

import static java.lang.String.format;
import static java.time.Duration.between;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.util.Sleeper;
import uk.gov.justice.framework.command.client.util.UtcClock;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
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

    public void runUntilComplete(final SystemCommanderMBean systemCommanderMBean, final UUID commandId, final String commandName) {

        final ZonedDateTime startTime = clock.now();

        int count = 0;
        while (! commandChecker.commandComplete(systemCommanderMBean, commandId, startTime)) {
            sleeper.sleepFor(1_000);
            count++;

            if (count % 10 == 0) {
                final long seconds = between(startTime, clock.now()).getSeconds();
                toConsolePrinter.println(format("%s running for %d seconds", commandName, seconds));
            }
        }
    }

}
