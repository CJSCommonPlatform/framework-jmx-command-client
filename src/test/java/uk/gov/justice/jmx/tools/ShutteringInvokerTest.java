package uk.gov.justice.jmx.tools;

import static java.util.Arrays.asList;
import static uk.gov.justice.jmx.tools.factory.MBeanFactory.createTestMBeans;
import static uk.gov.justice.jmx.tools.factory.MBeanFactory.matchingnames;

import uk.gov.justice.jmx.connector.MBeanHelper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ShutteringInvokerTest {

    @Test
    public void runShuttering() throws JMException, IOException {


        MBeanHelper mBeanHelper =  new MBeanHelper();
        final MBeanServer server = MBeanServerFactory.newMBeanServer();
        

        // Fill the MBeanServer with dummy MBean which will match
        createTestMBeans("shuttering", server);

        // Fill the MBeanServer with dummy MBean which will not match
        createTestMBeans("catchup", server);

        // compute expected result
        final Set<String> expectedResult = new HashSet<String>();
        for (String names : matchingnames()) {
            expectedResult.add(new ObjectName(names).getDomain());
        }

        // call my method
        final String[] testResult = mBeanHelper.getMbeanDomains(server);

        final Set<String> resultSet = new HashSet<String>();
        resultSet.addAll(asList(testResult));

        expectedResult.forEach(e -> verifyMbeanInDomain(e, resultSet));

    }

    private void verifyMbeanInDomain(final String e, final Set<String> testResult) {

        if (testResult.contains(e)) {
            return;
        }else{
            throw new RuntimeException("test failed");
        }
    }
}