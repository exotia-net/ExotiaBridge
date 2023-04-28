package net.exotia.bridge.shared.exceptions;

public class ServerIdIsInvalidException extends RuntimeException {
    public ServerIdIsInvalidException(String serverId) {
        super(String.format("%s not found!", serverId));
    }
}
