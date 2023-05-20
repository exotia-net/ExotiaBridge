package net.exotia.bridge.shared.http.exceptions;

public class HttpRequestException extends IllegalArgumentException {
    public HttpRequestException(String message) {
        super(message);
    }
}
