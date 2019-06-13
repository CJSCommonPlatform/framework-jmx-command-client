package uk.gov.justice;

import static java.lang.System.exit;

import uk.gov.justice.jmx.tools.ArgumentValidator;
import uk.gov.justice.jmx.tools.SystemCommandInvoker;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CatchUpAndShutteringManager {

    /**
     * @param args operation, host, port
     */
    public static void main(String... args) {
        final ArgumentValidator argumentValidator = new ArgumentValidator();

        final String command = args[0];
        final String host = args[1];
        final String port = args[2];

        argumentValidator.logCMDArgs(command, host, port);

        if(!argumentValidator.checkArgsNotNull(command, host, port)){
            exit(1);
        }

        SystemCommandInvoker systemCommandInvoker = new SystemCommandInvoker();
        systemCommandInvoker.runSystemCommand(command, host, port);
    }
}
