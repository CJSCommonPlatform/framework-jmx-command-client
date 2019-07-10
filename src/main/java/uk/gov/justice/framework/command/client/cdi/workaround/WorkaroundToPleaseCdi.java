package uk.gov.justice.framework.command.client.cdi.workaround;

import uk.gov.justice.services.messaging.jms.JmsCommandHandlerDestinationNameProvider;

public class WorkaroundToPleaseCdi implements JmsCommandHandlerDestinationNameProvider {

    @Override
    public String destinationName() {
        throw new UnsupportedOperationException("This class only exists to keep CDI happy and should never be called");
    }
}
