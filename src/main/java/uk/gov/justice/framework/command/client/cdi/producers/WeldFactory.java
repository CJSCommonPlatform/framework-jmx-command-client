package uk.gov.justice.framework.command.client.cdi.producers;

import org.jboss.weld.environment.se.Weld;

public class WeldFactory {

    public Weld create() {
        return new Weld();
    }
}
