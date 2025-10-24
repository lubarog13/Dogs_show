package org.example.model;

public class Person {
    private int id;
    private String name;
    private String surname;
    private String middlename;
    private String type;

    public Person(int id, String name, String surname, String middlename, String type) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middlename = middlename;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getSurname() {
        return surname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String toString() {
        return surname + " " + name + " " + middlename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}