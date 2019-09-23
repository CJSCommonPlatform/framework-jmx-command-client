package uk.gov.justice.framework.command.client.startup;

import uk.gov.justice.framework.command.client.MainApplication;
import uk.gov.justice.framework.command.client.cdi.producers.WeldFactory;

import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.inject.WeldInstance;

public class Bootstrapper {

    private final WeldFactory weldFactory;

    public Bootstrapper() {
        this(new WeldFactory());
    }

    public Bootstrapper(final WeldFactory weldFactory) {
        this.weldFactory = weldFactory;
    }

    public int startContainerAndRun(final String[] args) {
        try (final WeldContainer container = weldFactory.create().initialize()) {

            final WeldInstance<MainApplication> weldInstance = container.select(MainApplication.class);
            final MainApplication mainApplication = weldInstance.get();

            return mainApplication.run(args);
        }
    }
}
