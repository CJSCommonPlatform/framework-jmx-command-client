package uk.gov.justice.framework.command.tools;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;

import org.junit.Test;

public class SystemCommanderClientFactoryHelperTest {

    @Test
    public void shouldReturnSystemCommanderClientFactory2(){
        assertThat(new SystemCommanderClientFactoryHelper().makeSystemCommanderClientFactory(), instanceOf(SystemCommanderClientFactory.class));
    }

}