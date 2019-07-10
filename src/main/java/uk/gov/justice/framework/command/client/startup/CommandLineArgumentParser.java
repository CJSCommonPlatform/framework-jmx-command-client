package uk.gov.justice.framework.command.client.startup;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import uk.gov.justice.framework.command.client.CommandLineException;
import uk.gov.justice.framework.command.client.io.ToConsolePrinter;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineArgumentParser {

    @Inject
    private ToConsolePrinter toConsolePrinter;

    @Inject
    private Options options;

    @Inject
    private CommandLineParser commandLineParser;

    public Optional<CommandLine> parse(final String[] args) {

        try {
            final CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.hasOption("command") || commandLine.hasOption("list")) {
                return of(commandLine);
            }

            toConsolePrinter.println("No system command specifed.");

            return empty();

        } catch (final ParseException e) {
            throw new CommandLineException(format("Failed to parse command line args '%s'", asList(args)), e);
        }

    }

}
