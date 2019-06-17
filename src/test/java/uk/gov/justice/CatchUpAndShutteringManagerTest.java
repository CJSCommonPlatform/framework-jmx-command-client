package uk.gov.justice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatchUpAndShutteringManagerTest {
    private String host;
    private String port;

    @Before
    public void before() {
        host = "-localhost";
        port = "-9999";
    }

    @Test
    public void shouldReturnErrorWhenInvokingMain() {

        String [] args = {"-ch", "SHUTTER", "-ho", host, "-p", port};

        CatchUpAndShutteringManager.main(args);
    }
}