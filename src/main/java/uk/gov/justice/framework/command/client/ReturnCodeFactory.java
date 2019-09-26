package uk.gov.justice.framework.command.client;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.SystemCommandFailedException;
import uk.gov.justice.services.jmx.system.command.client.MBeanClientConnectionException;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxAuthenticationException;

import javax.inject.Inject;

public class ReturnCodeFactory {

    private static final int AUTHENTICATION_CODE = 1;
    private static final int CONNECTION_FAILED = 2;
    private static final int EXCEPTION_OCCURRED = 3;

    @Inject
    private ToConsolePrinter toConsolePrinter;

    public int createFor(final Exception exception) {

        if (exception instanceof JmxAuthenticationException) {
            toConsolePrinter.println("Authentication failed. Please ensure your username and password are correct");
            return AUTHENTICATION_CODE;
        }

        if (exception instanceof MBeanClientConnectionException) {
            toConsolePrinter.println(exception.getMessage());
            return CONNECTION_FAILED;
        }

        if (exception instanceof SystemCommandFailedException) {
            toConsolePrinter.printf(exception.getMessage());
            toConsolePrinter.println(((SystemCommandFailedException) exception).getServerStackTrace());
            return EXCEPTION_OCCURRED;
        }

        toConsolePrinter.println(exception);
        return EXCEPTION_OCCURRED;
    }
}
