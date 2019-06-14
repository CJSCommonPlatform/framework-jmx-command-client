package uk.gov.justice.framework.command.tools;

import static java.lang.Integer.parseInt;
import static uk.gov.justice.Operation.*;

import uk.gov.justice.Operation;
import uk.gov.justice.services.eventstore.management.catchup.commands.CatchupCommand;
import uk.gov.justice.services.jmx.command.BaseSystemCommand;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.management.shuttering.command.ShutterSystemCommand;
import uk.gov.justice.services.management.shuttering.command.UnshutterSystemCommand;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
public class SystemCommandInvoker {

    private static final String UNKNOWN = "UNKNOWN";
    private String commandName;

    @Inject
    private Logger logger;

    @Inject
    private EnumValidator enumValidator;

    @Inject
    private SystemCommanderClientFactoryHelper systemCommanderClientFactoryHelper;

    public void runSystemCommand(final String command, final String host, final String port) {

        final SystemCommanderClientFactory systemCommanderClientFactory = systemCommanderClientFactoryHelper.makeSystemCommanderClientFactory();

        try (final SystemCommanderClient systemCommanderClient = systemCommanderClientFactory.create(host, parseInt(port))) {

            if (!enumValidator.checkCommandIsValid(command)) {
                setUnKnownCommandName(command);
            } else {
                commandName = command;
            }

            Operation operation = valueOf(commandName);
            switch (operation) {
                case SHUTTER:
                    callSystemCommand(systemCommanderClient, new ShutterSystemCommand());
                    break;
                case UNSHUTTER:
                    callSystemCommand(systemCommanderClient, new UnshutterSystemCommand());
                    break;
                case CATCHUP:
                    callSystemCommand(systemCommanderClient, new CatchupCommand());
                    break;
                default:
                    logger.info("{} is not a valid system command", command);
                    break;
            }
        }

    }

    private void setUnKnownCommandName(final String command) {
        if (!command.equalsIgnoreCase(UNKNOWN)) {
            commandName = UNKNOWN;
        }
    }

    private static void callSystemCommand(final SystemCommanderClient systemCommanderClient, final BaseSystemCommand systemCommand) {
        systemCommanderClient
                .getRemote()
                .call(systemCommand);
    }
}
