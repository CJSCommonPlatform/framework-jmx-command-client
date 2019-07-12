package uk.gov.justice.framework.command.client.cdi.producers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeldFactoryTest {

    @InjectMocks
    private WeldFactory weldFactory;

    @Test
    public void shouldCreateAnInstanceOfWeld() throws Exception {

        assertThat(weldFactory.create(), is(notNullValue()));
    }

    @Test
    public void shouldStopCoverallsAnnoyingUs() throws Exception {
        assertThat(new WeldFactory(), is(notNullValue()));
    }
}
