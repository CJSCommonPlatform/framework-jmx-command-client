package uk.gov.justice.framework.command.client;

import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.List;
import java.util.Optional;

public class CommandLocator {


    public Optional<SystemCommand> lookupCommand(final String commandName, final List<SystemCommand> systemCommands) {

        return systemCommands.stream()
                .filter(systemCommand -> systemCommand.getName().equals(commandName))
                .findFirst();
    }
}
