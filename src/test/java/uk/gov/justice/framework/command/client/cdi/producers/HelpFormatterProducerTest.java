package uk.gov.justice.framework.command.client.cdi.producers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HelpFormatterProducerTest {

    @InjectMocks
    private HelpFormatterProducer helpFormatterProducer;

    @Test
    public void shouldCreateNewHelpFormatter() throws Exception {
        assertThat(helpFormatterProducer.helpFormatter(), is(notNullValue()));
    }
}
