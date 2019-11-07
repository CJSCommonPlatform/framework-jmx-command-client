package uk.gov.justice.framework.command.client;

import uk.gov.justice.framework.command.client.io.CommandPrinter;
import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.jmx.SystemCommandInvoker;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;

public class CommandExecutor {

    @Inject
    private SystemCommandInvoker systemCommandInvoker;

    @Inject
    private CommandPrinter commandPrinter;

    public void executeCommand(
            final CommandLine commandLine,
            final JmxParameters jmxParameters,
            final List<SystemCommandDetails> systemCommandDetails) {

        if (commandLine.hasOption("list")) {
            commandPrinter.printSystemCommands(systemCommandDetails);
        } else {
            final String commandName = commandLine.getOptionValue("command");
            systemCommandInvoker.runSystemCommand(commandName, jmxParameters);
        }
    }
}
