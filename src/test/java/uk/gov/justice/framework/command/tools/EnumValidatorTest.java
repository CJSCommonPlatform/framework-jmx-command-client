package uk.gov.justice.framework.command.tools;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EnumValidatorTest {

    @Test
    public void shouldCheckCommandIsValid() {
        assertThat(new EnumValidator().checkCommandIsValid("SHUTTER"), is(true));
    }

    @Test
    public void shouldCheckCommandIsInvalid(){
        assertThat(new EnumValidator().checkCommandIsValid("TEST"), is(false));
    }
}