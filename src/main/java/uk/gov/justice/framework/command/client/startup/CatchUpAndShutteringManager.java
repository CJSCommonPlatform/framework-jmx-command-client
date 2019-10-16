package uk.gov.justice.framework.command.client.startup;

public class CatchUpAndShutteringManager {

    public static void main(String... args) {
        System.exit(new Bootstrapper().startContainerAndRun(args).getCode());
    }
}
