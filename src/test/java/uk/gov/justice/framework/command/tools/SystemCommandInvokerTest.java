package uk.gov.justice.framework.command.tools;


import static java.lang.Integer.parseInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.Operation.REBUILD;

import uk.gov.justice.Operation;
import uk.gov.justice.services.jmx.command.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class SystemCommandInvokerTest {
    private static final String SHUTTER = "SHUTTER";
    private static final String UNSHUTTER = "UNSHUTTER";
    private static final String CATCHUP = "CATCHUP";
    private static final String REBUILD = "REBUILD";

    private String host;
    private String port;

    @Mock
    private Logger logger;

    @Mock
    private SystemCommanderClientFactoryHelper systemCommanderClientFactoryHelper;

    @Mock
    private SystemCommanderClientFactory systemCommanderClientFactory;

    @Mock
    private SystemCommanderClient systemCommanderClient;

    @Mock
    private SystemCommanderMBean systemCommanderMBean;

    @Mock
    private EnumValidator enumValidator;

    @InjectMocks
    SystemCommandInvoker systemCommandInvoker;

    @Before
    public void before() {
        host = "localhost";
        port = "9999";
    }

    @Test
    public void shouldCallShuttering(){

        when(enumValidator.checkCommandIsValid("SHUTTER")).thenReturn(true);

        when(systemCommanderClientFactoryHelper.makeSystemCommanderClientFactory()).thenReturn(systemCommanderClientFactory);

        doReturn(systemCommanderClient).when(systemCommanderClientFactory).create(host, parseInt(port));

        when(systemCommanderClient.getRemote()).thenReturn(systemCommanderMBean);

        systemCommandInvoker.runSystemCommand(SHUTTER, host, port);
    }

    @Test
    public void shouldCallUnShuttering(){

        when(enumValidator.checkCommandIsValid("UNSHUTTER")).thenReturn(true);

        when(systemCommanderClientFactoryHelper.makeSystemCommanderClientFactory()).thenReturn(systemCommanderClientFactory);

        doReturn(systemCommanderClient).when(systemCommanderClientFactory).create(host, parseInt(port));

        when(systemCommanderClient.getRemote()).thenReturn(systemCommanderMBean);

        systemCommandInvoker.runSystemCommand(UNSHUTTER, host, port);
    }

    @Test
    public void shouldCallCatchup(){

        when(enumValidator.checkCommandIsValid("CATCHUP")).thenReturn(true);

        when(systemCommanderClientFactoryHelper.makeSystemCommanderClientFactory()).thenReturn(systemCommanderClientFactory);

        doReturn(systemCommanderClient).when(systemCommanderClientFactory).create(host, parseInt(port));

        when(systemCommanderClient.getRemote()).thenReturn(systemCommanderMBean);

        systemCommandInvoker.runSystemCommand(CATCHUP, host, port);
    }

    @Test
    public void shouldCallRebuild(){

        when(enumValidator.checkCommandIsValid(REBUILD)).thenReturn(true);

        when(systemCommanderClientFactoryHelper.makeSystemCommanderClientFactory()).thenReturn(systemCommanderClientFactory);

        doReturn(systemCommanderClient).when(systemCommanderClientFactory).create(host, parseInt(port));

        when(systemCommanderClient.getRemote()).thenReturn(systemCommanderMBean);

        systemCommandInvoker.runSystemCommand(Operation.REBUILD.name(), host, port);
    }

    @Test
    public void shouldLogWhenCommandCannotBeCalled(){

        when(enumValidator.checkCommandIsValid("TEST")).thenReturn(false);

        when(systemCommanderClientFactoryHelper.makeSystemCommanderClientFactory()).thenReturn(systemCommanderClientFactory);

        doReturn(systemCommanderClient).when(systemCommanderClientFactory).create(host, parseInt(port));

        when(systemCommanderClient.getRemote()).thenReturn(systemCommanderMBean);

        systemCommandInvoker.runSystemCommand("TEST", host, port);

        verify(logger).info("{} is not a valid system command", "TEST");
    }

    @Test
    public void shouldLogWhenCommandIsUnknown(){

        when(enumValidator.checkCommandIsValid(Operation.UNKNOWN.name())).thenReturn(true);

        when(systemCommanderClientFactoryHelper.makeSystemCommanderClientFactory()).thenReturn(systemCommanderClientFactory);

        doReturn(systemCommanderClient).when(systemCommanderClientFactory).create(host, parseInt(port));

        when(systemCommanderClient.getRemote()).thenReturn(systemCommanderMBean);

        systemCommandInvoker.runSystemCommand("UNKNOWN", host, port);

        verify(logger).info("{} is not a valid system command", "UNKNOWN");
    }
}