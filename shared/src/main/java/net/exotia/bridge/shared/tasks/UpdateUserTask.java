package net.exotia.bridge.shared.tasks;

import net.exotia.bridge.shared.services.UserService;

public class UpdateUserTask implements Runnable{
    private UserService userService;

    public UpdateUserTask(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        this.userService.getUsersToUpdate().thenAccept(users -> {
            users.forEach(user -> {
                user.getUpdate().setUpdatable(false);
                user.getUpdate().setLast(System.currentTimeMillis());
                this.userService.save(user);
            });
        });
    }
}
