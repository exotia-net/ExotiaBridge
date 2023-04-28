package net.exotia.bridge.shared.services.responses;

import lombok.Getter;

@Getter
public class EconomyResponse {
    private int id;
    private int userId;
    private int balance;
    private String createdAt;
    private String updatedAt;
}
