package uk.gov.justice.framework.command.client.jmx;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.command.SystemCommand;
import uk.gov.justice.services.jmx.command.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxAuthenticationException;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ListCommandsInvoker {

    @Inject
    private SystemCommanderClientFactory systemCommanderClientFactory;

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public Optional<List<SystemCommand>> listSystemCommands(final JmxParameters jmxParameters) {

        toConsolePrinter.printf("Connecting to Wildfly instance at '%s' on port %d", jmxParameters.getHost(), jmxParameters.getPort());
        jmxParameters.getCredentials().ifPresent(credentials -> toConsolePrinter.printf("Connecting with credentials for user '%s'", credentials.getUsername()));

        try (final SystemCommanderClient systemCommanderClient = systemCommanderClientFactory.create(jmxParameters)) {
            final SystemCommanderMBean remote = systemCommanderClient.getRemote();

            toConsolePrinter.println("Connected to remote server");

            return of(remote.listCommands());

        } catch (final JmxAuthenticationException e) {
            toConsolePrinter.println("Authentication failed. Please ensure your username and password are correct");
            return empty();
        }
    }
}