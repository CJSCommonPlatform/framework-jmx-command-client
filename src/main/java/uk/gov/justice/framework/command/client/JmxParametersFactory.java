package uk.gov.justice.framework.command.client;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static uk.gov.justice.services.jmx.system.command.client.connection.JmxParametersBuilder.jmxParameters;

import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParametersBuilder;

import org.apache.commons.cli.CommandLine;

public class JmxParametersFactory {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "9990";

    public JmxParameters createFrom(final CommandLine commandLine) {

        if (!commandLine.hasOption("context-name")) {
            throw new CommandLineException("No context name provided. Please run using --context-name <name> or -cn <name> .");
        }

        final String contextName = commandLine.getOptionValue("context-name");
        final String host = commandLine.getOptionValue("host", DEFAULT_HOST);
        final int port = getPort(commandLine);

        final JmxParametersBuilder jmxParameters = jmxParameters()
                .withContextName(contextName)
                .withHost(host)
                .withPort(port);

        if (commandLine.hasOption("username")) {

            final String username = commandLine.getOptionValue("username");
            final String password = commandLine.getOptionValue("password");

            jmxParameters
                    .withUsername(username)
                    .withPassword(password);
        }

        return jmxParameters.build();
    }

    private int getPort(final CommandLine commandLine) {

        final String portString = commandLine.getOptionValue("port", DEFAULT_PORT);

        try {
            return parseInt(portString);

        } catch (final NumberFormatException e) {
            throw new CommandLineException(format("Port number '%s' is not a number", portString));
        }
    }
}
