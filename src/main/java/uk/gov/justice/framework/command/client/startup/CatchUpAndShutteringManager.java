package uk.gov.justice.framework.command.client.startup;

public class CatchUpAndShutteringManager {

    public static void main(String... args) {
        new Bootstrapper().startContainerAndRun(args);
    }
}
