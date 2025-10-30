package org.example.utils;

import javax.swing.*;

/**
 * Абстрактный базовый класс для форм редактирования данных.
 */
public abstract class BaseEditForm extends BaseForm{
    /**
     * Кнопка сохранения изменений
     */
    protected JButton saveButton;
    
    /**
     * Кнопка удаления записи
     */
    protected JButton deleteButton;
    
    /**
     * Кнопка отмены и закрытия формы
     */
    protected JButton cancelButton;
    
    /**
     * Конструктор базовой формы редактирования.
     */
    public BaseEditForm() {
        super(800, 600, false);
    }

    /**
     * Инициализирует обработчики событий для всех кнопок формы.
     * Назначает соответствующие методы-обработчики для каждой кнопки.
     */
    public void initButtons() {
        saveButton.addActionListener(e -> saveClick());
        deleteButton.addActionListener(e -> deleteClick());
        cancelButton.addActionListener(e -> cancelClick());
    }

    /**
     * Перезагружает данные формы.
     * Метод предназначен для переопределения в наследниках.
     */
    public void reload() {

    }

    /**
     * Абстрактный метод базовой инициализации формы.
     * Должен быть реализован в наследниках для настройки основных компонентов формы.
     */
    abstract protected void baseInit();
    
    /**
     * Абстрактный метод инициализации полей формы.
     * Должен быть реализован в наследниках для заполнения полей формы данными.
     */
    abstract protected void initFields();
    
    /**
     * Абстрактный метод обработки события нажатия кнопки сохранения.
     * Должен быть реализован в наследниках для сохранения данных.
     */
    abstract protected void saveClick();
    
    /**
     * Абстрактный метод обработки события нажатия кнопки удаления.
     * Должен быть реализован в наследниках для удаления данных.
     */
    abstract protected void deleteClick();


    /**
     * Закрывает текущую форму.
     */
    protected void cancelClick() {
        dispose();
    }
}
