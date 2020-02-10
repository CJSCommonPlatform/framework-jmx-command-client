package uk.gov.justice.framework.command.client;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.fromString;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;

public class CommandIdProvider {

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public Optional<UUID> getCommandId(final CommandLine commandLine) {
        final String commandId = commandLine.getOptionValue("attach");

        if (commandId == null) {
            toConsolePrinter.println("Please specify a command id to attach to");
        } else {
            try {
                return of(fromString(commandId));
            } catch (final IllegalArgumentException e) {
                toConsolePrinter.printf("Command id '%s' is not a UUID", commandId);
            }
        }

        return empty();
    }
}
