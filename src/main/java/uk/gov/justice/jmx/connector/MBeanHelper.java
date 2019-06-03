package uk.gov.justice.jmx.connector;

import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static javax.management.JMX.newMBeanProxy;
import static javax.management.remote.JMXConnectorFactory.connect;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;

public class MBeanHelper {

    private static final Logger logger = getLogger(MBeanHelper.class.getName());

    public <T> T getMbeanProxy(final MBeanServerConnection connection, final ObjectName objectName, final Class<T> mBeanInterface) {
        return newMBeanProxy(connection, objectName, mBeanInterface, true);
    }

    public String[] getMbeanDomains(final MBeanServerConnection connection) throws IOException {
        final String [] domains = connection.getDomains();
        final List<String> mbeanDomains = asList(domains);

        logger.info("MBean Domains: ");
        mbeanDomains.forEach(mbeanDomain -> logger.info("{}", mbeanDomain));

        return domains;
    }

    public void getMbeanOperations(final ObjectName objectName, final MBeanServerConnection connection) throws IOException, IntrospectionException, InstanceNotFoundException, ReflectionException {
        final MBeanInfo mBeanInfo = connection.getMBeanInfo(objectName);
        final MBeanOperationInfo[] operations = mBeanInfo.getOperations();
        final List<MBeanOperationInfo> mbeanOperations = asList(operations);

        logger.info("MBean Operations: ");
        mbeanOperations.forEach(mBeanOperationInfo -> logger.info(mBeanOperationInfo.getName()));
    }

    public JMXConnector getJMXConnector(final String host, final String port) throws IOException {

        final String urlString =
                getProperty("jmx.service.url","service:jmx:remote+http://" + host + ":" + port);
        final JMXServiceURL serviceURL = new JMXServiceURL(urlString);

        return connect(serviceURL, null);
    }
}
