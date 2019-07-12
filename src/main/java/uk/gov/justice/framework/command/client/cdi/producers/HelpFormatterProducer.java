package uk.gov.justice.framework.command.client.cdi.producers;

import javax.enterprise.inject.Produces;

import org.apache.commons.cli.HelpFormatter;

public class HelpFormatterProducer {

    @Produces
    public HelpFormatter helpFormatter() {
        return new HelpFormatter();
    }
}
