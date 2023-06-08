package net.exotia.bridge.shared.exceptions;

import java.util.UUID;

public class UndefinedUserException extends RuntimeException {
    public UndefinedUserException(UUID uuid) {
        super(String.format("User with uuid %s is null!", uuid.toString()));
    }
}
