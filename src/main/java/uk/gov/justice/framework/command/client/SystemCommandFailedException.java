package uk.gov.justice.framework.command.client;

public class SystemCommandFailedException extends RuntimeException {

    public SystemCommandFailedException(final String message) {
        super(message);
    }
}
