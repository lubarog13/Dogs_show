package org.example.model;

public class Owner {

    private int id;
    private String surname;
    private String name;
    private String middlename;

    public Owner(int id, String surname, String name, String middlename) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.middlename = middlename;
    }

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String toString() {
        return surname + " " + name + " " + middlename;
    }
}
