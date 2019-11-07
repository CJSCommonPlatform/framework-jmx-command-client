package uk.gov.justice.framework.command.client;

import static uk.gov.justice.framework.command.client.ReturnCode.SUCCESS;

import uk.gov.justice.framework.command.client.cdi.producers.OptionsFactory;
import uk.gov.justice.framework.command.client.jmx.ListCommandsInvoker;
import uk.gov.justice.framework.command.client.startup.CommandLineArgumentParser;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

public class MainApplication {

    @Inject
    private CommandLineArgumentParser commandLineArgumentParser;

    @Inject
    private JmxParametersFactory jmxParametersFactory;

    @Inject
    private ListCommandsInvoker listCommandsInvoker;

    @Inject
    private OptionsFactory optionsFactory;

    @Inject
    private HelpFormatter formatter;

    @Inject
    private CommandExecutor commandExecutor;

    @Inject
    private ReturnCodeFactory returnCodeFactory;

    public ReturnCode run(final String[] args) {

        final Optional<CommandLine> commandLineOptional = commandLineArgumentParser.parse(args);

        if (commandLineOptional.isPresent()) {

            final CommandLine commandLine = commandLineOptional.get();

            final JmxParameters jmxParameters = jmxParametersFactory.createFrom(commandLine);

            try {

                final Optional<List<SystemCommandDetails>> systemCommandsOptional = listCommandsInvoker.listSystemCommands(jmxParameters);
                systemCommandsOptional.ifPresent(systemCommands -> commandExecutor.executeCommand(commandLine, jmxParameters, systemCommands));

            } catch (final RuntimeException e) {
                return returnCodeFactory.createFor(e);
            }

        } else {
            formatter.printHelp("java -jar catchup-shuttering-manager.jar", optionsFactory.createOptions());
        }

        return SUCCESS;
    }
}
