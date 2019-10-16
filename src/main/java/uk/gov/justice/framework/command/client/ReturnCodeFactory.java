package uk.gov.justice.framework.command.client;

import static uk.gov.justice.framework.command.client.ReturnCode.AUTHENTICATION_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.COMMAND_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.CONNECTION_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.EXCEPTION_OCCURRED;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.system.command.client.MBeanClientConnectionException;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxAuthenticationException;

import javax.inject.Inject;

public class ReturnCodeFactory {


    @Inject
    private ToConsolePrinter toConsolePrinter;

    public ReturnCode createFor(final Exception exception) {

        if (exception instanceof JmxAuthenticationException) {
            toConsolePrinter.println("Authentication failed. Please ensure your username and password are correct");
            return AUTHENTICATION_FAILED;
        }

        if (exception instanceof MBeanClientConnectionException) {
            toConsolePrinter.println(exception.getMessage());
            return CONNECTION_FAILED;
        }

        if (exception instanceof SystemCommandFailedException) {
            return COMMAND_FAILED;
        }

        if (exception instanceof SystemCommandInvocationFailedException) {
            toConsolePrinter.printf(exception.getMessage());
            toConsolePrinter.println(((SystemCommandInvocationFailedException) exception).getServerStackTrace());
            return EXCEPTION_OCCURRED;
        }

        toConsolePrinter.println(exception);
        return EXCEPTION_OCCURRED;
    }
}
