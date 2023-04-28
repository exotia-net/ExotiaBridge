package net.exotia.bridge.shared;

public class Endpoints {
    public static final String AUTH_HEADER = "ExotiaKey";
    public static final String AUTH_ME = "/auth/me";
    public static final String AUTH_SIGNUP = "/auth/signUp";

    public static String getUri(String endpoint, ApiConfiguration configuration) {
        return String.join("", configuration.getBaseUrl(), endpoint);
    }
}
