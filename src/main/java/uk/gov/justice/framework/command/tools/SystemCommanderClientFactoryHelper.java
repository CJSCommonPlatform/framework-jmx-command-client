package uk.gov.justice.framework.command.tools;

import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;

public class SystemCommanderClientFactoryHelper {

    SystemCommanderClientFactory makeSystemCommanderClientFactory(){
        return new SystemCommanderClientFactory();
    }
}
