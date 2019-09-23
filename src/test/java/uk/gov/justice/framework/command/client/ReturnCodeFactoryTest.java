package uk.gov.justice.framework.command.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
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

        final int resultCode = returnCodeFactory.createFor(jmxAuthenticationException);

        assertThat(resultCode, is(1));
        verify(toConsolePrinter).println("Authentication failed. Please ensure your username and password are correct");
    }

    @Test
    public void shouldReturnCorrectCodeForMBeanClientConnectionException() {

        final int resultCode = returnCodeFactory.createFor(new MBeanClientConnectionException("Test"));

        assertThat(resultCode, is(2));

        verify(toConsolePrinter).println("Test");
    }

    @Test
    public void shouldReturnCorrectCodeForAnyOtherException() {

        final int resultCode = returnCodeFactory.createFor(new Exception("Test"));

        assertThat(resultCode, is(3));

        verify(toConsolePrinter).println("Test");
    }
}