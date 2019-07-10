package uk.gov.justice.framework.command.client.io;

import static org.mockito.Mockito.verify;

import java.io.PrintStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ToConsolePrinterTest {

    @Mock
    private PrintStream printStream;

    @InjectMocks
    private ToConsolePrinter toConsolePrinter;

    @Test
    public void shouldCallPrintfOnThePrintStream() throws Exception {

        final String format = "Fred %s is %s";
        final String arg_1 = "arg_1";
        final String arg_2 = "arg_2";

        toConsolePrinter.printf(format, arg_1, arg_2);

        verify(printStream).printf(format + "\n", arg_1, arg_2);
    }

    @Test
    public void shouldCallPrintlnOnThePrintStream() throws Exception {

        final Object format = "Fred is dead";

        toConsolePrinter.println(format);

        verify(printStream).println(format);
    }
}
