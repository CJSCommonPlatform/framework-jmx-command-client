package uk.gov.justice.framework.command.client.io;

import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;

import java.util.List;

import javax.inject.Inject;

public class CommandPrinter {

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public void printSystemCommands(final List<SystemCommandDetails> systemCommandDetails) {

        if (systemCommandDetails.isEmpty()) {
            toConsolePrinter.println("This instance of wildfly does not support any system commands");
        }

        toConsolePrinter.printf("This instance of wildfly supports the following %d commands:", systemCommandDetails.size());
        systemCommandDetails.forEach(this::printCommand);
    }

    private void printCommand(final SystemCommandDetails systemCommandDetails) {

        final String commandName = systemCommandDetails.getName() + ":";
        toConsolePrinter.printf("\t- %-20s%s", commandName, systemCommandDetails.getDescription());
    }
}
