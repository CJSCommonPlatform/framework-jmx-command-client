package uk.gov.justice.framework.command.client.util;

import static java.time.ZoneOffset.UTC;

import java.time.ZonedDateTime;

import javax.enterprise.context.ApplicationScoped;

/**
 * Implementation of a clock that always generates a {@link ZonedDateTime} in UTC.
 */
@ApplicationScoped
public class UtcClock implements Clock {

    @Override
    public ZonedDateTime now() {
        return ZonedDateTime.now(UTC);
    }
}
