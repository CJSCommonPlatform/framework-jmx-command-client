package uk.gov.justice.framework.command.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.framework.command.client.ReturnCode.SUCCESS;

import uk.gov.justice.framework.command.client.cdi.producers.WeldFactory;
import uk.gov.justice.framework.command.client.startup.Bootstrapper;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.inject.WeldInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BootstrapperTest {

    @Mock
    private WeldFactory weldFactory;

    @InjectMocks
    private Bootstrapper bootstrapper;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldStartTheWeldCdiContainerGetTheMainApplicationClassAndRun() throws Exception {

        final String[] args = {"some", "args"};

        final Weld weld = mock(Weld.class);
        final WeldContainer container = mock(WeldContainer.class);
        final WeldInstance<MainApplication> weldInstance = mock(WeldInstance.class);
        final MainApplication mainApplication = mock(MainApplication.class);

        when(weldFactory.create()).thenReturn(weld);
        when(weld.initialize()).thenReturn(container);
        when(container.select(MainApplication.class)).thenReturn(weldInstance);
        when(weldInstance.get()).thenReturn(mainApplication);
        when(mainApplication.run(args)).thenReturn(SUCCESS);

        assertThat(bootstrapper.startContainerAndRun(args), is(SUCCESS));
        verify(mainApplication).run(args);
    }
}
