package uk.gov.justice.framework.command.tools;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class CommandLineArgumentParserTest {
    private String host;
    private String port;


    @Before
    public void before() {
        host = "localhost";
        port = "9999";
    }

    @Test
    public void shouldReturnTrueWhenCommandLineArgumentsAreValid() {
        final String [] args = {"-c", "SHUTTER", "-ho", host, "-p", port};

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);

        final boolean parsed = commandLineArgumentParser.parse();

        assertThat(parsed, is(true));
    }

    @Test
    public void shouldReturnTrueWhenAllCommandLineArgumentsAreGivenAndValid() {
        final String [] args = {"-h", "-c", "SHUTTER", "-ho", host, "-p", port};

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);

        final boolean parsed = commandLineArgumentParser.parse();

        assertThat(parsed, is(true));
    }

    @Test
    public void shouldReturnFalseWhenCommandLineArgumentIsEmpty() {
        final String [] args = {"-c", "", "-ho", host, "-p", port};

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);

        final boolean parsed = commandLineArgumentParser.parse();

        assertThat(parsed, is(false));
    }

    @Test
    public void shouldReturnFalseWhenCommandLineArgumentsParseFails() {
        final String [] args = {"-g", "SHUTTER", "-ho", host, "-p", port};

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);

        final boolean parsed = commandLineArgumentParser.parse();

        assertThat(parsed, is(false));
    }
}