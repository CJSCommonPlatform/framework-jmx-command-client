package uk.gov.justice.framework.command.client.io;

import java.io.PrintStream;

public class ToConsolePrinter {

    private final PrintStream printStream;

    public ToConsolePrinter() {
        this(System.out);
    }

    public ToConsolePrinter(final PrintStream printStream) {
        this.printStream = printStream;
    }

    public void printf(final String format, final Object... args) {
        printStream.printf(format + "\n", args);
    }

    public void println(final Exception e) {
        printStream.println(e.getMessage());
        e.printStackTrace(printStream);
    }

    public void println(final Object x) {
        printStream.println(x);
    }
}
