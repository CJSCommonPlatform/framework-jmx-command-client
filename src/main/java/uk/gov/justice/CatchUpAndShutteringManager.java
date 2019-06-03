package uk.gov.justice;

import static org.slf4j.LoggerFactory.getLogger;
import static uk.gov.justice.Operations.CATCHUP;
import static uk.gov.justice.Operations.SHUTTER;
import static uk.gov.justice.Operations.UNSHUTTER;
import static uk.gov.justice.jmx.tools.CatchUpInvoker.runCatchup;
import static uk.gov.justice.jmx.tools.ShutteringInvoker.runShuttering;

import org.slf4j.Logger;

public class CatchUpAndShutteringManager {

    private static final Logger logger = getLogger(CatchUpAndShutteringManager.class.getName());

    /**
     *
     * @param args operation, host, port
     * @throws Exception
     */
    public static void main(String... args) throws Exception {

        final String operation = args[0];
        final String host = args[1];
        final String port = args[2];

        logger.info("Operation: {}", operation);
        logger.info("Host: {}", host);
        logger.info("Port: {}", port);

        if(operation.equalsIgnoreCase(SHUTTER.name())){
            runShuttering(true, host, port);
        }
        if(operation.equalsIgnoreCase(UNSHUTTER.name())){
            runShuttering(false, host, port);
        }
        if(operation.equalsIgnoreCase(CATCHUP.name())){
            runCatchup(host, port);
        }
    }
}
