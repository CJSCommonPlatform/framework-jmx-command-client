package uk.gov.justice.framework.command.client.jmx;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SystemCommandAttacher {

    @Inject
    private SystemCommanderClientFactory systemCommanderClientFactory;

    @Inject
    private SystemCommandAttachRunner systemCommandAttachRunner;

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public void attachToRunningCommand(final UUID commandId, final JmxParameters jmxParameters) {

        final String contextName = jmxParameters.getContextName();
        toConsolePrinter.printf("Attaching to system command with id'%s'", commandId);
        toConsolePrinter.printf("Connecting to %s context at '%s' on port %d", contextName, jmxParameters.getHost(), jmxParameters.getPort());

        jmxParameters.getCredentials().ifPresent(credentials -> toConsolePrinter.printf("Connecting with credentials for user '%s'", credentials.getUsername()));

        try (final SystemCommanderClient systemCommanderClient = systemCommanderClientFactory.create(jmxParameters)) {
            final SystemCommanderMBean systemCommanderMBean = systemCommanderClient.getRemote(contextName);
            systemCommandAttachRunner.attach(commandId, systemCommanderMBean);
        } catch (final SystemCommandInvocationFailedException e) {
            toConsolePrinter.printf("The command with id '%s' failed: %s", commandId, e.getMessage());
            toConsolePrinter.println(e.getServerStackTrace());
            throw e;
        }
    }
}
