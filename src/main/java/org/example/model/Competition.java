package org.example.model;

public class Competition {
    private int id;
    private int place;
    private String dogName;
    private String dogBreed;
    private String ownerName;
    private String judgeName;

    public Competition(int id, int place, String dogName, String dogBreed, String ownerName, String judgeName) {
        this.id = id;
        this.place = place;
        this.dogName = dogName;
        this.dogBreed = dogBreed;
        this.ownerName = ownerName;
        this.judgeName = judgeName;
    }
    
    public int getId() {
        return id;
    }
    public int getPlace() {
        return place;
    }
    public String getDogName() {
        return dogName;
    }
    public String getDogBreed() {
        return dogBreed;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public String getJudgeName() {
        return judgeName;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setPlace(int place) {
        this.place = place;
    }
    public void setDogName(String dogName) {
        this.dogName = dogName;
    }
    public void setDogBreed(String dogBreed) {
        this.dogBreed = dogBreed;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }
}