package org.example;

import java.util.UUID;

public class UserUtils {
    static void notifyUser(UUID userId, String message) {
        System.out.println("Уведомление для пользователя " + userId + ": " + message);
    }
}
