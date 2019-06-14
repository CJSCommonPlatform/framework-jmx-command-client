package uk.gov.justice.framework.command.tools;

import uk.gov.justice.Operation;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnumValidator {


    public boolean checkCommandIsValid(final String command) {
        return contains(Operation.class, command);
    }

    private static boolean contains(Class<? extends Enum> clazz, String val) {
        Enum[] arr = clazz.getEnumConstants();
        for (Enum operation : arr) {
            if (operation.name().equals(val)) {
                return true;
            }
        }
        return false;
    }
}
