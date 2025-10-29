package org.example.model;

/**
 * Класс, представляющий собаку в системе.
 * Содержит информацию об идентификаторе, кличке, породе и владельце.
 */
public class Dog {
    private int id;
    private String name;
    private String breed;
    private Person owner;

    /**
     * Конструктор для создания объекта Dog.
     *
     * @param id идентификатор собаки
     * @param name кличка собаки
     * @param breed порода собаки
     * @param owner владелец собаки
     */
    public Dog(int id, String name, String breed, Person owner) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.owner = owner;
    }

    /**
     * Возвращает идентификатор собаки.
     *
     * @return идентификатор
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает кличку собаки.
     *
     * @return кличка
     */
    public String getName() {
        return name;
    }
    
    /**
     * Возвращает породу собаки.
     *
     * @return порода
     */
    public String getBreed() {
        return breed;
    }

    /**
     * Возвращает владельца собаки.
     *
     * @return объект Person, представляющий владельца
     */
    public Person getOwner() {
        return owner;
    }

    /**
     * Устанавливает идентификатор собаки.
     *
     * @param id идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Устанавливает кличку собаки.
     *
     * @param name кличка
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает породу собаки.
     *
     * @param breed порода
     */
    public void setBreed(String breed) {
        this.breed = breed;
    }

    /**
     * Устанавливает владельца собаки.
     *
     * @param owner объект Person, представляющий владельца
     */
    public void setOwner(Person owner) {
        this.owner = owner;
    }

    /**
     * Возвращает строковое представление собаки в формате "[id] кличка порода".
     *
     * @return строковое представление собаки
     */
    public String toString() {
        return "[" + id + "] " + name + " " + breed;
    }
}