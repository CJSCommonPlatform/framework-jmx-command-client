package uk.gov.justice;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.justice.framework.command.tools.CommandLineArgumentParser;
import uk.gov.justice.framework.command.tools.SystemCommandInvoker;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;

@ApplicationScoped
public class CatchUpAndShutteringManager {

    private static final Logger logger = getLogger(CatchUpAndShutteringManager.class.getName());

    /**
     * @param args operation, host, port
     */
    public static void main(String... args) {
        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);
        final boolean argumentParsed = commandLineArgumentParser.parse();

        if(argumentParsed){
            final String command = args[0];
            final String host = args[1];
            final String port = args[2];

            final SystemCommandInvoker systemCommandInvoker = new SystemCommandInvoker();

            systemCommandInvoker.runSystemCommand(command, host, port);

            logger.info("java -jar catchup-shuttering-manager.jar -c -{} -ho -{} -p -{} invoked successfully", command, host, port);
        }
        else{
            logger.error("java -jar catchup-shuttering-manager.jar not invoked successfully!!! Please check command line arguments & usage");
        }
    }
}
