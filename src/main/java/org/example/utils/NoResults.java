package org.example.utils;

/**
 * Исключение, которое выбрасывается, если не найдено ничего
 */
public class NoResults extends RuntimeException {
    /**
     * Конструктор исключения
     */
    public NoResults() {
        super("Ничего не найдено");
    }
}
