package uk.gov.justice.framework.command.client.cdi.producers;

import javax.enterprise.inject.Produces;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLineParser;

public class CommandLineParserProducer {

    @Produces
    public CommandLineParser commandLineParser() {
        return new BasicParser();
    }
}
