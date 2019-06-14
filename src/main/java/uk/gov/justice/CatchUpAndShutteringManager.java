package uk.gov.justice;

import uk.gov.justice.framework.command.tools.CommandLineArgumentParser;
import uk.gov.justice.framework.command.tools.SystemCommandInvoker;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CatchUpAndShutteringManager {

    /**
     * @param args operation, host, port
     */
    public static void main(String... args) {

        boolean argumentParsed = new CommandLineArgumentParser(args).parse();

        if(argumentParsed){
            final String command = args[0];
            final String host = args[1];
            final String port = args[2];

            SystemCommandInvoker systemCommandInvoker = new SystemCommandInvoker();

            systemCommandInvoker.runSystemCommand(command, host, port);
        }
        else{
            System.exit(1);
        }
    }
}
