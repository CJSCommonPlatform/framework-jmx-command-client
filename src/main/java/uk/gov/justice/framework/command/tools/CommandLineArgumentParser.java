package uk.gov.justice.framework.command.tools;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;


public class CommandLineArgumentParser {

    private static final Logger logger = getLogger(CommandLineArgumentParser.class.getName());

    private String[] args;
    private Options options = new Options();


    public CommandLineArgumentParser(String[] args) {

        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("c", "command", true, "Framework command to execute, for example CATCHUP,SHUTTER, UNSHUTTER & REBUILD.");
        options.addOption("ho", "host", true, "Host Remote or Localhost");
        options.addOption("p", "port", true, "Wildfly management port");
    }

    public boolean parse() {
        CommandLineParser parser = new BasicParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")){
                help();
            }

            if (isCommandNullOrEmpty(cmd) && isHostNullOrEmpty(cmd) && isPortNullOrEmpty(cmd)) {
                return true;
            } else {
                help();
                return false;
            }

        } catch (ParseException e) {
            logger.info("Failed to parse command line properties", e);
            help();
            return false;
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("CatchUpAndShutteringManager", options);
    }

    private boolean isCommandNullOrEmpty(CommandLine cmd){
        return cmd.hasOption("c") &&
                (isNotBlank(cmd.getOptionValue("c")));
    }

    private boolean isHostNullOrEmpty(CommandLine cmd){
        return cmd.hasOption("ho") &&
                isNotBlank(cmd.getOptionValue("ho"));
    }

    private boolean isPortNullOrEmpty(CommandLine cmd){
        return cmd.hasOption("p") &&
                isNotBlank(cmd.getOptionValue("p"));
    }
}
