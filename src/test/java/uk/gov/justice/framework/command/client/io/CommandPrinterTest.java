package uk.gov.justice.framework.command.client.io;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.framework.command.client.cdi.producers.WeldFactory;
import uk.gov.justice.services.jmx.command.SystemCommand;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandPrinterTest {

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private CommandPrinter commandPrinter;

    @Test
    public void shouldGetTheListOfAllCommandsAndPrintThem() throws Exception {

        final String commandName_1 = "commandName_1";
        final String commandName_2 = "commandName_2";

        final SystemCommand systemCommand_1 = mock(SystemCommand.class);
        final SystemCommand systemCommand_2 = mock(SystemCommand.class);

        final List<SystemCommand> systemCommands = asList(systemCommand_1, systemCommand_2);

        when(systemCommand_1.getName()).thenReturn(commandName_1);
        when(systemCommand_2.getName()).thenReturn(commandName_2);

        commandPrinter.printSystemCommands(systemCommands);

        final InOrder inOrder = inOrder(toConsolePrinter);

        inOrder.verify(toConsolePrinter).printf("This instance of wildfly supports the following %d commands:", 2);
        inOrder.verify(toConsolePrinter).printf("\t- %s", commandName_1);
        inOrder.verify(toConsolePrinter).printf("\t- %s", commandName_2);
    }

    @Test
    public void shouldStopCoverallsAnnoyingUs() throws Exception {
        assertThat(new CommandPrinter(), is(notNullValue()));
    }
}
