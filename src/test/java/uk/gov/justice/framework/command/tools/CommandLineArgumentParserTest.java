package uk.gov.justice.framework.command.tools;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
        String [] args = {"-c", "SHUTTER", "-ho", host, "-p", port};

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);

        final boolean parsed = commandLineArgumentParser.parse();

        assertThat(parsed, is(true));
    }

    @Test
    public void shouldReturnTrueWhenAllCommandLineArgumentsAreGivenAndValid() {
        String [] args = {"-h", "-c", "SHUTTER", "-ho", host, "-p", port};

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);

        final boolean parsed = commandLineArgumentParser.parse();

        assertThat(parsed, is(true));
    }

    @Test
    public void shouldReturnFalseWhenCommandLineArgumentIsEmpty() {
        String [] args = {"-c", "", "-ho", host, "-p", port};

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);

        final boolean parsed = commandLineArgumentParser.parse();

        assertThat(parsed, is(false));
    }

    @Test
    public void shouldReturnFalseWhenCommandLineArgumentsParseFails() {
        String [] args = {"-g", "SHUTTER", "-ho", host, "-p", port};

        final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(args);

        final boolean parsed = commandLineArgumentParser.parse();

        assertThat(parsed, is(false));
    }
}