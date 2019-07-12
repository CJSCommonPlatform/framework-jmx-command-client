package uk.gov.justice.framework.command.client.cdi.producers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandLineParserProducerTest {

    @InjectMocks
    private CommandLineParserProducer commandLineParserProducer;

    @Test
    public void shouldCreateNewCommandLineParserProducer() throws Exception {
        assertThat(commandLineParserProducer.commandLineParser(),is(notNullValue()));
    }
}
