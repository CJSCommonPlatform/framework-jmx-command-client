package uk.gov.justice.framework.command.client.jmx;

import static java.util.Optional.of;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
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

    public Optional<List<SystemCommandDetails>> listSystemCommands(final JmxParameters jmxParameters) {

        final String contextName = jmxParameters.getContextName();

        toConsolePrinter.printf("Connecting to %s context at '%s' on port %d", contextName, jmxParameters.getHost(), jmxParameters.getPort());
        jmxParameters.getCredentials().ifPresent(credentials -> toConsolePrinter.printf("Connecting with credentials for user '%s'", credentials.getUsername()));

        try (final SystemCommanderClient systemCommanderClient = systemCommanderClientFactory.create(jmxParameters)) {
            final SystemCommanderMBean remote = systemCommanderClient.getRemote(contextName);

            toConsolePrinter.printf("Connected to %s context", contextName);

            return of(remote.listCommands());
        }
    }
}
