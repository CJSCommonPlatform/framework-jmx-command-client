package uk.gov.justice.framework.command.client;

public class CommandLineException extends RuntimeException {

    public CommandLineException(final String message) {
        super(message);
    }

    public CommandLineException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
