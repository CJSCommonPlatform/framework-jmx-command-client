package uk.gov.justice.framework.command.client.jmx;

import static java.lang.String.format;
import static java.time.Duration.between;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_COMPLETE;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_FAILED;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.jmx.api.domain.CommandState;
import uk.gov.justice.services.jmx.api.domain.SystemCommandStatus;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.inject.Inject;

public class CommandChecker {

    @Inject
    private ToConsolePrinter toConsolePrinter;

    @Inject
    private UtcClock clock;

    public boolean commandComplete(final SystemCommanderMBean systemCommanderMBean, final UUID commandId, final ZonedDateTime startTime) {

        final SystemCommandStatus commandStatus = systemCommanderMBean.getCommandStatus(commandId);


        final CommandState commandState = commandStatus.getCommandState();
        if (commandState == COMMAND_COMPLETE ) {

            final ZonedDateTime endTime = clock.now();
            final long durationMillis = between(startTime, endTime).toMillis();

            final String duration = formatDurationHMS(durationMillis);
            toConsolePrinter.println(format("Command %s complete", commandStatus.getSystemCommandName()));
            toConsolePrinter.println(format("%s duration %s (hours:minutes:seconds:milliseconds)", commandStatus.getSystemCommandName(), duration));
            return true;
        }

        if(commandState == COMMAND_FAILED) {
            final long durationMillis = between(startTime, clock.now()).toMillis();

            final String duration = formatDurationHMS(durationMillis);
            toConsolePrinter.println(format("ERROR: Command %s failed", commandStatus.getSystemCommandName()));
            toConsolePrinter.println(format("ERROR: %s", commandStatus.getMessage()));
            toConsolePrinter.println(format("%s duration %s (hours:minutes:seconds:milliseconds)", commandStatus.getSystemCommandName(), duration));
            return true;
        }

        return false;
    }

}
