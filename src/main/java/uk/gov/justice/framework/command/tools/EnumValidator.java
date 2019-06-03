package uk.gov.justice.framework.command.tools;

import uk.gov.justice.Operation;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnumValidator {

    boolean checkCommandIsValid(final String command) {
        return contains(Operation.class, command);
    }

    private boolean contains(final Class<? extends Enum> clazz, final String val) {
        final Enum[] arr = clazz.getEnumConstants();
        for (final Enum operation : arr) {
            if (operation.name().equals(val)) {
                return true;
            }
        }
        return false;
    }
}
