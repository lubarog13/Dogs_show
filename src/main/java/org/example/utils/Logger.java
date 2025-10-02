package org.example.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Класс для ведения журнала
 *
 */
public class Logger {
    /**
     * Путь к файлу
     */
    private static String path;

    /**
     * Конструктор класса
     * 
     * @param path
     */
    public Logger(String path) {
        Logger.path = path;
        if (!new File(path).exists()) {
            try {
                new File(path).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод для записи сообщения в файл
     * 
     * @param message
     */
    public static void log(String message) {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(LocalDateTime.now() + " " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для получения пути к файлу
     * 
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Метод для установки пути к файлу
     * 
     * @param path
     */
    public void setPath(String path) {
        Logger.path = path;
    }

}