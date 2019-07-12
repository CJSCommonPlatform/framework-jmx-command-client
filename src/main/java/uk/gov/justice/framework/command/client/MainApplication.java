package uk.gov.justice.framework.command.client;

import uk.gov.justice.framework.command.client.jmx.ListCommandsInvoker;
import uk.gov.justice.framework.command.client.startup.CommandLineArgumentParser;
import uk.gov.justice.services.jmx.command.SystemCommand;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class MainApplication {

    @Inject
    private CommandLineArgumentParser commandLineArgumentParser;

    @Inject
    private JmxParametersFactory jmxParametersFactory;

    @Inject
    private ListCommandsInvoker listCommandsInvoker;

    @Inject
    private Options options;

    @Inject
    private HelpFormatter formatter;

    @Inject
    private CommandExecutor commandExecutor;

    public void run(final String[] args) {

        final Optional<CommandLine> commandLineOptional = commandLineArgumentParser.parse(args);

        if (commandLineOptional.isPresent()) {

            final CommandLine commandLine = commandLineOptional.get();

            final JmxParameters jmxParameters = jmxParametersFactory.createFrom(commandLine);

            final Optional<List<SystemCommand>> systemCommandsOptional = listCommandsInvoker.listSystemCommands(jmxParameters);
            systemCommandsOptional.ifPresent(systemCommands -> commandExecutor.executeCommand(commandLine, jmxParameters, systemCommands));

        } else {
            formatter.printHelp("java -jar catchup-shuttering-manager.jar", options);
        }
    }
}
