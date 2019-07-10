package uk.gov.justice.framework.command.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.startup.CommandLineArgumentParser;

import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandLineArgumentParserTest {

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @Mock
    private Options options;

    @Mock
    private CommandLineParser commandLineParser;

    @InjectMocks
    private CommandLineArgumentParser commandLineArgumentParser;

    @Test
    public void shouldParseTheCommandLineArgumentsAndReturnTheCommandLineObjectIfCommandIsSpecified() throws Exception {

        final String[] args = {"some", "args"};
        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLineParser.parse(options, args)).thenReturn(commandLine);
        when(commandLine.hasOption("command")).thenReturn(true);
        when(commandLine.hasOption("list")).thenReturn(false);

        final Optional<CommandLine> commandLineOptional = commandLineArgumentParser.parse(args);

        if (commandLineOptional.isPresent()) {
            assertThat(commandLineOptional.get(), is(commandLine));
        } else {
            fail();
        }
    }

    @Test
    public void shouldParseTheCommandLineArgumentsAndReturnTheCommandLineObjectIfListIsSpecified() throws Exception {

        final String[] args = {"some", "args"};
        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLineParser.parse(options, args)).thenReturn(commandLine);
        when(commandLine.hasOption("command")).thenReturn(false);
        when(commandLine.hasOption("list")).thenReturn(true);

        final Optional<CommandLine> commandLineOptional = commandLineArgumentParser.parse(args);

        if (commandLineOptional.isPresent()) {
            assertThat(commandLineOptional.get(), is(commandLine));
        } else {
            fail();
        }
    }

    @Test
    public void shouldReturnEmptyIfNeitherTheCommandOptionNorTheListOptionIsSpecified() throws Exception {

        final String[] args = {"some", "args"};
        final CommandLine commandLine = mock(CommandLine.class);

        when(commandLineParser.parse(options, args)).thenReturn(commandLine);
        when(commandLine.hasOption("command")).thenReturn(false);
        when(commandLine.hasOption("list")).thenReturn(false);

        final Optional<CommandLine> commandLineOptional = commandLineArgumentParser.parse(args);

        assertThat(commandLineOptional.isPresent(), is(false));

        verify(toConsolePrinter).println("No system command specifed.");
    }

    @Test
    public void shouldThrowExceptionIfParsingTheCommandLineFails() throws Exception {

        final ParseException parseException = new ParseException("ooops");

        final String[] args = {"some", "args"};

        when(commandLineParser.parse(options, args)).thenThrow(parseException);

        try {
            commandLineArgumentParser.parse(args);
            fail();
        } catch (final CommandLineException expected) {
            assertThat(expected.getCause(), is(parseException));
            assertThat(expected.getMessage(), is("Failed to parse command line args '[some, args]'"));
        }
    }
}
