package uk.gov.justice.jmx.tools.factory;

import uk.gov.justice.services.jmx.Catchup;
import uk.gov.justice.services.jmx.Shuttering;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class MBeanFactory {

    private static final String SHUTTERING = "shuttering";
    private static final String CATCHUP = "catchup";


    public static void createTestMBeans(String domain, MBeanServer server)
            throws JMException {

        ObjectName objectName = null;
        if (domain.equalsIgnoreCase(SHUTTERING)) {
            objectName = new ObjectName(domain, "type", Shuttering.class.getSimpleName());
            server.registerMBean(new Shuttering(), objectName);
        } else if (domain.equalsIgnoreCase(CATCHUP)) {
            objectName = new ObjectName(domain, "type", Catchup.class.getSimpleName());
            server.registerMBean(new Catchup(), objectName);
        }

    }

    public static String[] matchingnames() {
        return new String[]{
                "shuttering:type=Shuttering",
                "catchup:type=Catchup"
        };
    }
}
