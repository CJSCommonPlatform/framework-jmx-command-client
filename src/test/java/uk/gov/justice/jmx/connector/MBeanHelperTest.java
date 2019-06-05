package uk.gov.justice.jmx.connector;

import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.jmx.ShutteringMBean;

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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MBeanHelperTest {

    @Mock
    private JMXConnector jmxConnector;

    @Mock
    private JMXConnectionHelperFactory jmxConnectionHelperFactory;

    @Mock
    private MBeanServerConnection mBeanServerConnection;

    @Mock
    private ObjectName objectName;

    @Mock
    private MBeanInfo mBeanInfo;

    @Mock
    private JMXServiceURL jmxServiceURL;

    @InjectMocks
    private MBeanHelper mBeanHelper;

    @Test
    public void getMbeanProxy() {

        final ShutteringMBean mbeanProxy = mBeanHelper.getMbeanProxy(mBeanServerConnection, objectName, ShutteringMBean.class);

        assertThat(mbeanProxy, instanceOf(ShutteringMBean.class));
    }

    @Test
    public void getMbeanDomains() throws IOException {
        final String [] domains = {"domain1", "domain2", "domain3"};
        when(mBeanServerConnection.getDomains()).thenReturn(domains);

        final String[] mbeanDomains = mBeanHelper.getMbeanDomains(mBeanServerConnection);
        final List<String> mbeanDomainList = asList(mbeanDomains);

        assertThat(mbeanDomainList, hasSize(3));
        assertThat(mbeanDomainList, containsInAnyOrder("domain1", "domain2", "domain3"));
    }

    @Test
    public void getMbeanOperations() throws IntrospectionException, ReflectionException, InstanceNotFoundException, IOException {

        MBeanOperationInfo mBeanOperationInfo = mock(MBeanOperationInfo.class);
        MBeanOperationInfo[] operations = {mBeanOperationInfo};

        when(mBeanServerConnection.getMBeanInfo(objectName)).thenReturn(mBeanInfo);
        when(mBeanInfo.getOperations()).thenReturn(operations);

        mBeanHelper.getMbeanOperations(objectName, mBeanServerConnection);
    }

    @Test
    public void getJMXConnector() throws IOException {

        final String host = "localhost";
        final String port = "9999";
        final String urlString =
                getProperty("jmx.service.url","service:jmx:remote+http://" + host + ":" + port);

        when(jmxConnectionHelperFactory.createJMXServiceURL(urlString)).thenReturn(jmxServiceURL);

        when(jmxConnectionHelperFactory.connect(jmxServiceURL, null)).thenReturn(jmxConnector);

        final JMXConnector jmxConnector = mBeanHelper.getJMXConnector(host, port);

        assertNotNull(jmxConnector);

        jmxConnector.close();
    }
}