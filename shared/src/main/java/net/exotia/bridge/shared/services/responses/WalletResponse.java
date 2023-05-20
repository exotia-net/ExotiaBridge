package net.exotia.bridge.shared.services.responses;

import lombok.Getter;

@Getter
public class WalletResponse {
    private int id;
    private int userId;
    private float coins;
    private float spentCoins;
    private String createdAt;
    private String updatedAt;
}
