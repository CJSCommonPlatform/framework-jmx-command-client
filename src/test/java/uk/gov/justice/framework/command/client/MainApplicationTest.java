package uk.gov.justice.framework.command.client;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.framework.command.client.ReturnCode.AUTHENTICATION_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.SUCCESS;

import uk.gov.justice.framework.command.client.cdi.producers.OptionsFactory;
import uk.gov.justice.framework.command.client.jmx.ListCommandsInvoker;
import uk.gov.justice.framework.command.client.startup.CommandLineArgumentParser;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MainApplicationTest {

    @Mock
    private CommandLineArgumentParser commandLineArgumentParser;

    @Mock
    private JmxParametersFactory jmxParametersFactory;

    @Mock
    private ListCommandsInvoker listCommandsInvoker;

    @Mock
    private OptionsFactory optionsFactory;

    @Mock
    private HelpFormatter formatter;

    @Mock
    private CommandExecutor commandExecutor;

    @Mock
    private ReturnCodeFactory returnCodeFactory;

    @InjectMocks
    private MainApplication mainApplication;


    @Test
    public void shouldLookupTheCorrectCommandAndInvokeIt() throws Exception {

        final String[] args = {"some", "args"};
        final JmxParameters jmxParameters = mock(JmxParameters.class);

        final CommandLine commandLine = mock(CommandLine.class);
        final SystemCommandDetails systemCommandDetails_1 = mock(SystemCommandDetails.class);
        final SystemCommandDetails systemCommandDetails_2 = mock(SystemCommandDetails.class);

        final List<SystemCommandDetails> systemCommands = asList(systemCommandDetails_1, systemCommandDetails_2);

        when(commandLineArgumentParser.parse(args)).thenReturn(of(commandLine));
        when(jmxParametersFactory.createFrom(commandLine)).thenReturn(jmxParameters);
        when(listCommandsInvoker.listSystemCommands(jmxParameters)).thenReturn(of(systemCommands));

        assertThat(mainApplication.run(args), is(SUCCESS));

        verify(commandExecutor).executeCommand(commandLine, jmxParameters, systemCommands);
        verifyZeroInteractions(formatter);
    }

    @Test
    public void shouldPrintHelpIfParsingCommandFailsOrHelpIsSpecified() throws Exception {

        final String[] args = {"some", "args"};

        final Options options = mock(Options.class);

        when(optionsFactory.createOptions()).thenReturn(options);
        when(commandLineArgumentParser.parse(args)).thenReturn(empty());

        mainApplication.run(args);

        verify(formatter).printHelp("java -jar catchup-shuttering-manager.jar", options);

        verifyZeroInteractions(commandExecutor);
    }

    @Test
    public void shouldRespondWithCorrectReturnCodeOnException() {

        final String[] args = {"some", "args"};
        final JmxParameters jmxParameters = mock(JmxParameters.class);

        final CommandLine commandLine = mock(CommandLine.class);
        final RuntimeException runtimeException = new RuntimeException();

        when(commandLineArgumentParser.parse(args)).thenReturn(of(commandLine));
        when(jmxParametersFactory.createFrom(commandLine)).thenReturn(jmxParameters);
        when(listCommandsInvoker.listSystemCommands(jmxParameters)).thenThrow(runtimeException);
        when(returnCodeFactory.createFor(runtimeException)).thenReturn(AUTHENTICATION_FAILED);

        assertThat(mainApplication.run(args), is(AUTHENTICATION_FAILED));

        verifyZeroInteractions(commandExecutor);
        verifyZeroInteractions(formatter);
    }
}
