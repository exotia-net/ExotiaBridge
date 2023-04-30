package net.exotia.bridge.proxy.service;

import java.util.ArrayList;
import java.util.List;

public class UpdatableService {
    private List<UpdatableUser> updatableUsers = new ArrayList<>();

    public void addUpdatableUser(UpdatableUser user) {
        this.updatableUsers.add(user);
    }
    public List<UpdatableUser> getUpdatableUsers() {
        return this.updatableUsers;
    }
}
