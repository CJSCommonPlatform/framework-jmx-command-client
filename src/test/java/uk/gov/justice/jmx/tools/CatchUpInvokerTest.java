package uk.gov.justice.jmx.tools;

import static org.mockito.Mockito.doReturn;

import uk.gov.justice.jmx.connector.MBeanHelper;
import uk.gov.justice.services.jmx.Catchup;
import uk.gov.justice.services.jmx.CatchupMBean;
import uk.gov.justice.services.jmx.Shuttering;
import uk.gov.justice.services.jmx.ShutteringMBean;

import java.io.IOException;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatchUpInvokerTest {
    private static final String CATCHUP = "catchup";

    @Mock
    private JMXConnector jmxConnector;

    @Mock
    private MBeanServerConnection mBeanServerConnection;

    @Mock
    private MBeanHelper mBeanHelper;

    @Mock
    private CatchupMBean catchupMBean;

    @InjectMocks
    CatchUpInvoker catchUpInvoker;

    @Test
    public void runCatchup() throws JMException, IOException {

        final String host = "localhost";
        final String port = "9999";
        final ObjectName objectName = new ObjectName(CATCHUP, "type", Catchup.class.getSimpleName());

        doReturn(jmxConnector).when(mBeanHelper).getJMXConnector(host, port);

        doReturn(mBeanServerConnection).when(jmxConnector).getMBeanServerConnection();

        doReturn(catchupMBean)
                .when(mBeanHelper).getMbeanProxy(mBeanServerConnection, objectName, CatchupMBean.class);

        catchUpInvoker.runCatchup(host, port);
    }
}