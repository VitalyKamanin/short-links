package org.example;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {


    private static final String CONFIG_FILE = "config.yaml";
    private static Map<String, Object> config;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                config = yaml.load(inputStream);
                System.out.println("Конфигурационный файл YAML загружен: " + CONFIG_FILE);
            } else {
                System.out.println("Не удалось найти конфигурационный файл YAML: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке конфигурационного файла YAML: " + CONFIG_FILE + e);
        }
    }

    public static String getString(String key) {
        return (String) getNestedValue(key);
    }

    public static int getInt(String key) {
        Object value = getNestedValue(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                System.out.println("Не удалось преобразовать значение ключа " + key + " в число. Возвращается 0." + e);
                return 0;
            }
        } else {
            System.out.println("Значение ключа " + key + " не является числом. Возвращается 0.");
            return 0;
        }
    }

    private static Object getNestedValue(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> current = config;
        for (int i = 0; i < keys.length - 1; i++) {
            Object next = current.get(keys[i]);
            if (next instanceof Map) {
                current = (Map<String, Object>) next;
            } else {
                System.out.println("Ключ " + key + " не найден в конфигурации.");
                return null;
            }
        }
        return current.get(keys[keys.length - 1]);
    }
}