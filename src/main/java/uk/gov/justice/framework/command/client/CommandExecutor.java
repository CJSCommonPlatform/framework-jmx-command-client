package uk.gov.justice.framework.command.client;

import static java.util.UUID.fromString;

import uk.gov.justice.framework.command.client.io.CommandPrinter;
import uk.gov.justice.framework.command.client.jmx.SystemCommandAttacher;
import uk.gov.justice.framework.command.client.jmx.SystemCommandInvoker;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;

public class CommandExecutor {

    @Inject
    private SystemCommandInvoker systemCommandInvoker;

    @Inject
    private SystemCommandAttacher systemCommandAttacher;

    @Inject
    private CommandPrinter commandPrinter;

    public void executeCommand(
            final CommandLine commandLine,
            final JmxParameters jmxParameters,
            final List<SystemCommandDetails> systemCommandDetails) {

        if (commandLine.hasOption("list")) {
            commandPrinter.printSystemCommands(systemCommandDetails);
        } else if (commandLine.hasOption("attach")) {
            final UUID commandId = fromString(commandLine.getOptionValue("attach"));
            systemCommandAttacher.attachToRunningCommand(commandId, jmxParameters);
        } else {
            final String commandName = commandLine.getOptionValue("command");
            systemCommandInvoker.runSystemCommand(commandName, jmxParameters);
        }
    }


}
