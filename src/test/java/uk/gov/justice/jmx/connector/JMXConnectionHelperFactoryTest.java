package uk.gov.justice.jmx.connector;

import static java.lang.System.getProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.net.MalformedURLException;

import javax.management.remote.JMXServiceURL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JMXConnectionHelperFactoryTest {

    @Test
    public void shouldCreateJMXServiceURL() throws MalformedURLException {
        JMXConnectionHelperFactory jmxConnectionHelperFactory = new JMXConnectionHelperFactory();

        final String host = "localhost";
        final String port = "9999";
        final String urlString =
                getProperty("jmx.service.url","service:jmx:remote+http://" + host + ":" + port);

        JMXServiceURL jmxServiceURL = jmxConnectionHelperFactory.createJMXServiceURL(urlString);

        assertThat(jmxServiceURL, instanceOf(JMXServiceURL.class));
    }
}