package uk.gov.justice.framework.command.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.gov.justice.framework.command.client.ReturnCode.AUTHENTICATION_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.COMMAND_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.CONNECTION_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.EXCEPTION_OCCURRED;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.system.command.client.MBeanClientConnectionException;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxAuthenticationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReturnCodeFactoryTest {

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private ReturnCodeFactory returnCodeFactory;

    @Test
    public void shouldReturnCorrectCodeForJmxAuthenticationException() {

        final JmxAuthenticationException jmxAuthenticationException = new JmxAuthenticationException("Test", new Exception());

        assertThat(returnCodeFactory.createFor(jmxAuthenticationException), is(AUTHENTICATION_FAILED));
        verify(toConsolePrinter).println("Authentication failed. Please ensure your username and password are correct");
    }

    @Test
    public void shouldReturnCorrectCodeForMBeanClientConnectionException() {

        assertThat(returnCodeFactory.createFor(new MBeanClientConnectionException("Test")), is(CONNECTION_FAILED));

        verify(toConsolePrinter).println("Test");
    }

    @Test
    public void shouldReturnCorrectCodeForSystemCommandFailedException() {

        assertThat(returnCodeFactory.createFor(new SystemCommandFailedException("Test")), is(COMMAND_FAILED));

        verifyZeroInteractions(toConsolePrinter);
    }

    @Test
    public void shouldReturnCorrectCodeForSystemCommandInvocationFailedException() {

        assertThat(returnCodeFactory.createFor(new SystemCommandInvocationFailedException("Test", "Stack Trace")), is(EXCEPTION_OCCURRED));

        verify(toConsolePrinter).printf("Test");
        verify(toConsolePrinter).println("Stack Trace");
    }

    @Test
    public void shouldReturnCorrectCodeForAnyOtherException() {

        final Exception exception = new Exception("Test");

        assertThat(returnCodeFactory.createFor(exception), is(EXCEPTION_OCCURRED));

        verify(toConsolePrinter).println(exception);
    }
}
