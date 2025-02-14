package org.example;

import org.apache.commons.lang3.RandomStringUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

import static org.example.ConfigLoader.*;
import static org.example.Main.*;
import static org.example.UserUtils.notifyUser;

public class LinkUtils {
    private static final Map<UUID, Set<String>> users = new HashMap<>();

    public static String createLink(String longUrl, UUID userId, int maxClicks, int lifetime) {
        String shortLink;
        do {
            shortLink = RandomStringUtils.randomAlphanumeric(getInt("shortlink.length"));
        } while (links.containsKey(shortLink));

        LinkData linkData = new LinkData(longUrl, chooseLocalDateTime(lifetime), chooseMaxClicks(maxClicks), userId);
        links.put(shortLink, linkData);

        users.computeIfAbsent(userId, k -> new HashSet<>()).add(shortLink);

        System.out.println("Создана короткая ссылка " + shortLink + " для пользователя " + userId);
        return getString("base.url") + shortLink;
    }

    private static int chooseMaxClicks(int maxClicks) {
        return Math.max(maxClicks, getInt("maxClicks"));
    }

    private static LocalDateTime chooseLocalDateTime(int lifetime) {
        LocalDateTime userDefinedLifetime = LocalDateTime.now().plusHours(lifetime);
        LocalDateTime appDefinedLifetime = LocalDateTime.now().plusHours(getInt("link.expiry.hours"));
        return appDefinedLifetime.isBefore(userDefinedLifetime) ? appDefinedLifetime : userDefinedLifetime;
    }

    public static void redirect(String shortLink) {
        String identifier = shortLink.substring(getString("base.url").length());
        LinkData linkData = links.get(identifier);

        if (linkData == null) {
            System.out.println("Ссылка не найдена.");
            return;
        }

        if (LocalDateTime.now().isAfter(linkData.expiryTime)) {
            System.out.println("Срок действия ссылки истек.");
            links.remove(identifier);
            notifyUser(linkData.userId, "Ссылка " + shortLink + " устарела и была удалена.");
            return;
        }

        if (linkData.clicksRemaining <= 0) {
            System.out.println("Лимит переходов исчерпан.");
            notifyUser(linkData.userId, "Лимит переходов для ссылки " + shortLink + " исчерпан.");
            return;
        }

        linkData.clicksRemaining--;

        try {
            Desktop.getDesktop().browse(new URI(linkData.longUrl));
            System.out.println("Перенаправление на " + linkData.longUrl);
        } catch (IOException | URISyntaxException e) {
            System.err.println("Ошибка при перенаправлении: " + e.getMessage());
        }
    }
}
