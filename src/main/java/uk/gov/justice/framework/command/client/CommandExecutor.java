package uk.gov.justice.framework.command.client;

import uk.gov.justice.framework.command.client.io.CommandPrinter;
import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.jmx.SystemCommandInvoker;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;

public class CommandExecutor {

    @Inject
    private SystemCommandInvoker systemCommandInvoker;

    @Inject
    private CommandLocator commandLocator;

    @Inject
    private ToConsolePrinter toConsolePrinter;

    @Inject
    private CommandPrinter commandPrinter;

    public void executeCommand(
            final CommandLine commandLine,
            final JmxParameters jmxParameters,
            final List<SystemCommand> systemCommands) {

        if (commandLine.hasOption("list")) {
            commandPrinter.printSystemCommands(systemCommands);
        } else {
            final String commandName = commandLine.getOptionValue("command");
            final Optional<SystemCommand> command = commandLocator.lookupCommand(commandName, systemCommands);

            if (command.isPresent()) {
                systemCommandInvoker.runSystemCommand(command.get(), jmxParameters);
            } else {
                toConsolePrinter.printf("No command found with name '%s'", commandName);
                commandPrinter.printSystemCommands(systemCommands);
            }
        }
    }
}
