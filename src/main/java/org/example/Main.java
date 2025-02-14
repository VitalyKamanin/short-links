package org.example;

import java.util.*;

import static org.example.LinkUtils.createLink;
import static org.example.LinkUtils.redirect;
import static org.example.SchedulerUtils.clean;
import static org.example.SchedulerUtils.timer;

public class Main {

    static final Map<String, LinkData> links = new HashMap<>();

    public static void main(String[] args) {
        clean();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Создать короткую ссылку");
            System.out.println("2. Перейти по ссылке");
            System.out.println("3. Выход");

            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Неверный выбор.");
            }
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Введите URL: ");
                    String longUrl = scanner.nextLine();
                    System.out.println("Введите лимит переходов: ");
                    int maxClicks = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Введите время жизни ссылки: ");
                    int lifetime = scanner.nextInt();
                    scanner.nextLine();

                    UUID userId = null;
                    System.out.print("Введите ваш UUID (или оставьте пустым для генерации нового): ");
                    String uuidInput = scanner.nextLine();
                    if (uuidInput != null && !uuidInput.trim().isEmpty()) {
                        try {
                            userId = UUID.fromString(uuidInput.trim());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный формат UUID. Будет сгенерирован новый.");
                        }
                    }

                    if (userId == null) {
                        userId = UUID.randomUUID();
                        System.out.println("Сгенерирован новый UUID: " + userId);
                    }

                    String shortLink = createLink(longUrl, userId, maxClicks, lifetime);
                    System.out.println("Короткая ссылка: " + shortLink);
                    System.out.println("Ваш UUID: " + userId);
                    break;
                case 2:
                    System.out.println("Введите короткую ссылку: ");
                    String shortLinkToRedirect = scanner.nextLine();
                    redirect(shortLinkToRedirect);
                    break;
                case 3:
                    System.out.println("Выход...");
                    timer.cancel();
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }
}