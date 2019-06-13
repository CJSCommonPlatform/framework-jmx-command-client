package uk.gov.justice.jmx.tools;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentValidatorTest {

    @Mock
    private Logger logger;

    private String host;
    private String port;

    @InjectMocks
    private ArgumentValidator argumentValidator;

    @Before
    public void before() {
        host = "localhost";
        port = "9999";
    }

    @Test
    public void shouldLogCMDArgs() {
        argumentValidator.logCMDArgs("SHUTTER", host, port);
        verify(logger, times(1)).info("Command: {}", "SHUTTER");
        verify(logger, times(1)).info("Host: {}", host);
        verify(logger, times(1)).info("Port: {}", port);
    }

    @Test
    public void shouldReturnTrueWhenArgsNotNull() {
        assertThat(argumentValidator.checkArgsNotNull("SHUTTER", host, port), is(true));
    }

    @Test
    public void shouldReturnFalseWhenArgsNull() {
        assertThat(argumentValidator.checkArgsNotNull(null, host, port), is(false));
    }

    @Test
    public void shouldCheckCommandIsValid() {
        assertThat(argumentValidator.checkCommandIsValid("SHUTTER", host, port), is(true));
    }

    @Test
    public void shouldCheckCommandIsInvalid(){
        assertThat(argumentValidator.checkCommandIsValid("TEST", host, port), is(false));
    }
}