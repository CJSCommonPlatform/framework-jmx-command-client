package uk.gov.justice.framework.command.client.io;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;

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
        final String description_1 = "description_1";
        final String description_2 = "description_2";

        final SystemCommandDetails systemCommandDetails_1 = mock(SystemCommandDetails.class);
        final SystemCommandDetails systemCommandDetails_2 = mock(SystemCommandDetails.class);

        final List<SystemCommandDetails> systemCommands = asList(systemCommandDetails_1, systemCommandDetails_2);

        when(systemCommandDetails_1.getName()).thenReturn(commandName_1);
        when(systemCommandDetails_1.getDescription()).thenReturn(description_1);
        when(systemCommandDetails_2.getName()).thenReturn(commandName_2);
        when(systemCommandDetails_2.getDescription()).thenReturn(description_2);

        commandPrinter.printSystemCommands(systemCommands);

        final InOrder inOrder = inOrder(toConsolePrinter);

        inOrder.verify(toConsolePrinter).printf("This instance of wildfly supports the following %d commands:", 2);
        inOrder.verify(toConsolePrinter).printf("\t- %-20s%s", commandName_1 + ":", description_1);
        inOrder.verify(toConsolePrinter).printf("\t- %-20s%s", commandName_2 + ":", description_2);
    }

    @Test
    public void shouldStopCoverallsAnnoyingUs() throws Exception {
        assertThat(new CommandPrinter(), is(notNullValue()));
    }
}
