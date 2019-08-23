package uk.gov.justice.framework.command.client.cdi.producers;

import org.apache.commons.cli.Options;

public class OptionsFactory {

    public Options createOptions() {
        final Options options = new Options();

        options.addOption("cn", "context-name", true, "The name of the context on which to run the command. Required");
        options.addOption("c", "command", true, "Framework command to execute. Run with --list for a list of all commands");
        options.addOption("h", "host", true, "Hostname or IP address of the Wildfly server. Defaults to localhost");
        options.addOption("p", "port", true, "Wildfly management port. Defaults to 9990");
        options.addOption("u", "username", true, "Optional username for Wildfly management security");
        options.addOption("pw", "password", true, "Optional password for Wildfly management security");

        options.addOption("help", false, "Show help.");
        options.addOption("l", "list", false, "List of all framework commands");

        return options;
    }
}
