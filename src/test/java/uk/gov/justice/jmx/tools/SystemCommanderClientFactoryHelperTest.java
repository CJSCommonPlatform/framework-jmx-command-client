package uk.gov.justice.jmx.tools;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemCommanderClientFactoryHelperTest {

    @InjectMocks
    private SystemCommanderClientFactoryHelper systemCommanderClientFactoryHelper;

    @Test
    public void shouldReturnSystemCommanderClientFactory(){

        SystemCommanderClientFactory systemCommanderClientFactory = systemCommanderClientFactoryHelper.makeSystemCommanderClientFactory();

        assertThat(systemCommanderClientFactory, instanceOf(SystemCommanderClientFactory.class));
    }

}