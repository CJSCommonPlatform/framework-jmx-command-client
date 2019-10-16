package uk.gov.justice.framework.command.client.util;

import java.time.ZonedDateTime;

/**
 * Interface for clock providers.
 */
public interface Clock {

    ZonedDateTime now();
}
