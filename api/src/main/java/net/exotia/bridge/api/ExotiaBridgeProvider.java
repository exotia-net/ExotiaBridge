package net.exotia.bridge.api;

public class ExotiaBridgeProvider {
    private static ExotiaBridgeInstance exotiaBridgeInstance;

    public static void setProvider(ExotiaBridgeInstance exotiaBridgeInstance) {
        ExotiaBridgeProvider.exotiaBridgeInstance = exotiaBridgeInstance;
    }

    public static ExotiaBridgeInstance getProvider() {
        if (exotiaBridgeInstance == null)
            throw new NullPointerException("ExotiaBridge instance is null.");
        return exotiaBridgeInstance;
    }
}
