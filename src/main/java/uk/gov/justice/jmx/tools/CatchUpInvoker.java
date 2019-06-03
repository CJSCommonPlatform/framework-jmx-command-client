package uk.gov.justice.jmx.tools;

import static javax.management.JMX.newMBeanProxy;

import uk.gov.justice.jmx.connector.MBeanHelper;
import uk.gov.justice.services.jmx.Catchup;
import uk.gov.justice.services.jmx.CatchupMBean;

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;

public class CatchUpInvoker {
    private CatchUpInvoker() {}

    private static final String CATCHUP = "catchup";

    private static MBeanHelper mBeanHelper = new MBeanHelper();

    public static void runCatchup(final String host, final String port) throws IOException, MalformedObjectNameException, IntrospectionException, InstanceNotFoundException, ReflectionException {

        try (final JMXConnector jmxConnector = mBeanHelper.getJMXConnector(host, port)) {
            final MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();

            final ObjectName objectName = new ObjectName(CATCHUP, "type", Catchup.class.getSimpleName());

            mBeanHelper.getMbeanDomains(connection);

            mBeanHelper.getMbeanOperations(objectName, connection);

            final CatchupMBean catchupMBean = newMBeanProxy(connection, objectName, CatchupMBean.class, true);

            catchupMBean.doCatchupRequested();
        }
    }
}
