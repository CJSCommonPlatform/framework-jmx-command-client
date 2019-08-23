package uk.gov.justice.framework.command.client.io;

import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.List;

import javax.inject.Inject;

public class CommandPrinter {

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public void printSystemCommands(final List<SystemCommand> commands) {

        if (commands.isEmpty()) {
            toConsolePrinter.println("This instance of wildfly does not support any system commands");
        }

        toConsolePrinter.printf("This instance of wildfly supports the following %d commands:", commands.size());
        commands.forEach(this::printCommand);
    }

    private void printCommand(final SystemCommand systemCommand) {

        final String commandName = systemCommand.getName() + ":";
        toConsolePrinter.printf("\t- %-20s%s", commandName, systemCommand.getDescription());
    }
}
