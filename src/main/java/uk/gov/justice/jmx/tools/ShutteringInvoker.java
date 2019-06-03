package uk.gov.justice.jmx.tools;

import uk.gov.justice.jmx.connector.MBeanHelper;
import uk.gov.justice.services.jmx.Shuttering;
import uk.gov.justice.services.jmx.ShutteringMBean;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;


public class ShutteringInvoker {
    private ShutteringInvoker(){}

    private static final String SHUTTERING = "shuttering";

    private static MBeanHelper mBeanHelper = new MBeanHelper();

    public static  void runShuttering(final boolean isShutteringRequired, final String host, final String port) throws IOException, MalformedObjectNameException {
        try(final JMXConnector jmxConnector = mBeanHelper.getJMXConnector(host, port)){

            final MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();

            final ObjectName objectName = new ObjectName(SHUTTERING, "type", Shuttering.class.getSimpleName());

            if(isShutteringRequired) {
                mBeanHelper.getMbeanProxy(connection, objectName, ShutteringMBean.class).doShutteringRequested();
            } else {
                mBeanHelper.getMbeanProxy(connection, objectName, ShutteringMBean.class).doUnshutteringRequested();
            }
        }
    }
}
