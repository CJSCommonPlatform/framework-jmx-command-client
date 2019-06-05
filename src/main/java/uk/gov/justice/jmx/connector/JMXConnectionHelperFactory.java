package uk.gov.justice.jmx.connector;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

public class JMXConnectionHelperFactory {

    JMXServiceURL createJMXServiceURL(String urlString) throws MalformedURLException {
        return new JMXServiceURL(urlString);
    }

    JMXConnector connect(final JMXServiceURL serviceURL, final Object object) throws IOException {
        return javax.management.remote.JMXConnectorFactory.connect(serviceURL, null);
    }
}
