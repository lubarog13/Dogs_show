package org.example.utils;

/**
 * Исключение, которое выбрасывается, если введен пустой поисковый запрос
 */
public class EmptySearchInput extends RuntimeException {
    /**
     * Конструктор исключения
     */
    public EmptySearchInput() {
        super("Введен пустой поисковый запрос");
    }
}
