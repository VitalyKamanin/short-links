package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.ConfigLoader.getString;
import static org.example.Main.links;
import static org.example.UserUtils.notifyUser;

public class SchedulerUtils {
    static final Timer timer = new Timer();

    static void clean() {
        timer.schedule(new TimerTask() {
            @Override public void run() {
                System.out.println("Запуск очистки устаревших ссылок...");
                LocalDateTime now = LocalDateTime.now();
                List<String> expiredLinks = new ArrayList<>();

                links.forEach((key, value) -> {
                    if (now.isAfter(value.expiryTime)) {
                        expiredLinks.add(key);
                        notifyUser(value.userId, "Ссылка " + getString("base.url") + key + " устарела и была удалена.");
                    }
                });

                expiredLinks.forEach(links::remove);

                System.out.println("Очистка завершена. Удалено ссылок: " + expiredLinks.size());
            }
        }, 0, ConfigLoader.getInt("cleanup.interval.millis"));
        System.out.println("Задача очистки устаревших ссылок запущена.");
    }
}
