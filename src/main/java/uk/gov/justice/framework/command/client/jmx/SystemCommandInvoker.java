package uk.gov.justice.framework.command.client.jmx;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.command.SystemCommand;
import uk.gov.justice.services.jmx.command.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxAuthenticationException;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SystemCommandInvoker {

    @Inject
    private SystemCommanderClientFactory systemCommanderClientFactory;

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public void runSystemCommand(final SystemCommand systemCommand, final JmxParameters jmxParameters) {

        toConsolePrinter.printf("Running system command '%s'", systemCommand.getName());

        toConsolePrinter.printf("Connecting to Wildfly instance at '%s' on port %d", jmxParameters.getHost(), jmxParameters.getPort());
        jmxParameters.getCredentials().ifPresent(credentials -> toConsolePrinter.printf("Connecting with credentials for user '%s'", credentials.getUsername()));

        try (final SystemCommanderClient systemCommanderClient = systemCommanderClientFactory.create(jmxParameters)) {
            final SystemCommanderMBean systemCommanderMBean = systemCommanderClient.getRemote();

            toConsolePrinter.println("Connected to remote server");

            systemCommanderMBean.call(systemCommand);

            toConsolePrinter.printf("System command '%s' successfully sent", systemCommand.getName());

        } catch (final JmxAuthenticationException e) {
            toConsolePrinter.println("Authentication failed. Please ensure your username and password are correct");
        } 
    }
}
