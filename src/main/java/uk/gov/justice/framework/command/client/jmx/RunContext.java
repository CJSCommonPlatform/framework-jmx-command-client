package uk.gov.justice.framework.command.client.jmx;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class RunContext {

    private final UUID commandId;
    private final String commandName;
    private final ZonedDateTime startTime;

    public RunContext(final UUID commandId, final String commandName, final ZonedDateTime startTime) {
        this.commandId = commandId;
        this.commandName = commandName;
        this.startTime = startTime;
    }

    public UUID getCommandId() {
        return commandId;
    }

    public String getCommandName() {
        return commandName;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RunContext)) return false;
        final RunContext that = (RunContext) o;
        return Objects.equals(commandId, that.commandId) &&
                Objects.equals(commandName, that.commandName) &&
                Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandId, commandName, startTime);
    }

    @Override
    public String toString() {
        return "RunContext{" +
                "commandId=" + commandId +
                ", commandName='" + commandName + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
