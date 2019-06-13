package uk.gov.justice.jmx.tools;

import uk.gov.justice.Operation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
public class ArgumentValidator {

    @Inject
    private Logger logger;

    public void logCMDArgs(final String command, final String host, final String port) {
        logger.info("Command: {}", command);
        logger.info("Host: {}", host);
        logger.info("Port: {}", port);
    }

    public boolean checkArgsNotNull(final String command, final String host, final String port) {
        if (command == null || host == null || port == null) {
            logger.info("The args passed cannot be null. Please check java command line args!!!");
            logCMDArgs(command, host, port);
            return false;
        }
        return true;
    }

    public boolean checkCommandIsValid(final String command, final String host, final String port) {
        if (!contains(Operation.class, command)) {
            logger.info("The command arg is invalid. Please provide a valid system command!!!");
            logCMDArgs(command, host, port);
            return false;
        }
        return true;
    }

    private static boolean contains(Class<? extends Enum> clazz, String val) {
        Object[] arr = clazz.getEnumConstants();
        for (Object e : arr) {
            if (((Enum) e).name().equals(val)) {
                return true;
            }
        }
        return false;
    }
}
