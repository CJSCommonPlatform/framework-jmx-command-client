package uk.gov.justice.framework.command.client.jmx;

import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_IN_PROGRESS;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_RECEIVED;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.CommandNotFoundException;
import uk.gov.justice.services.jmx.api.domain.CommandState;
import uk.gov.justice.services.jmx.api.domain.SystemCommandStatus;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.inject.Inject;

public class SystemCommandAttachRunner {

    @Inject
    private CommandPoller commandPoller;

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public void attach(final UUID commandId, final SystemCommanderMBean systemCommanderMBean) {
        try {
            final SystemCommandStatus commandStatus = systemCommanderMBean.getCommandStatus(commandId);
            final String commandName = commandStatus.getSystemCommandName();
            final CommandState commandState = commandStatus.getCommandState();
            
            if (commandState == COMMAND_IN_PROGRESS || commandState == COMMAND_RECEIVED) {
                final ZonedDateTime startTime = commandStatus.getStatusChangedAt();
                final RunContext runContext = new RunContext(
                        commandId,
                        commandName,
                        startTime
                );

                commandPoller.runUntilComplete(systemCommanderMBean, runContext);
            } else {
                toConsolePrinter.printf("Cannot attach to Command with id '%s'. Command is not running. Current command status: %s", commandId, commandState);
            }
        } catch (final CommandNotFoundException e) {
            toConsolePrinter.printf("No system command exists with id '%s", commandId);
        }
    }
}
