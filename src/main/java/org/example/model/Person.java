package org.example.model;

/**
 * Класс, представляющий человека в системе (владельца собаки или судью).
 * Содержит информацию об идентификаторе, имени, фамилии, отчестве и типе персоны.
 */
public class Person {
    private int id;
    private String name;
    private String surname;
    private String middlename;
    private String type;

    /**
     * Конструктор для создания объекта Person.
     *
     * @param id идентификатор персоны
     * @param name имя
     * @param surname фамилия
     * @param middlename отчество
     * @param type тип персоны ("owner" для владельца, "judge" для судьи)
     */
    public Person(int id, String name, String surname, String middlename, String type) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middlename = middlename;
        this.type = type;
    }

    /**
     * Возвращает идентификатор персоны.
     *
     * @return идентификатор
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает имя персоны.
     *
     * @return имя
     */
    public String getName() {
        return name;
    }
    
    /**
     * Возвращает фамилию персоны.
     *
     * @return фамилия
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Возвращает отчество персоны.
     *
     * @return отчество
     */
    public String getMiddlename() {
        return middlename;
    }

    /**
     * Устанавливает идентификатор персоны.
     *
     * @param id идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Устанавливает имя персоны.
     *
     * @param name имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает фамилию персоны.
     *
     * @param surname фамилия
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Устанавливает отчество персоны.
     *
     * @param middlename отчество
     */
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    /**
     * Возвращает строковое представление персоны в формате "[id] фамилия имя отчество".
     *
     * @return строковое представление персоны
     */
    public String toString() {
        return "[" + id + "] " + surname + " " + name + " " + middlename;
    }

    /**
     * Возвращает тип персоны.
     *
     * @return тип ("owner" для владельца, "judge" для судьи)
     */
    public String getType() {
        return type;
    }

    /**
     * Устанавливает тип персоны.
     *
     * @param type тип ("owner" для владельца, "judge" для судьи)
     */
    public void setType(String type) {
        this.type = type;
    }
}