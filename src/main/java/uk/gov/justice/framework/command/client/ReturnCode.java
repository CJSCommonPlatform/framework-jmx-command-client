package uk.gov.justice.framework.command.client;

public enum ReturnCode {

    SUCCESS(0),
    AUTHENTICATION_FAILED(1),
    CONNECTION_FAILED(2),
    COMMAND_FAILED(3),
    EXCEPTION_OCCURRED(4);

    private int code;

    ReturnCode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
