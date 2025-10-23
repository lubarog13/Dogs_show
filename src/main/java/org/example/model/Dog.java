package org.example.model;

public class Dog {
    private int id;
    private String name;
    private String breed;
    private Owner owner;

    public Dog(int id, String name, String breed, Owner owner) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getBreed() {
        return breed;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String toString() {
        return name + " " + breed;
    }
}