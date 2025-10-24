package org.example.model;

public class Competition {
    private int id;
    private int place;
    private int dogId;
    private int judgeId;
    private String dogName;
    private String dogBreed;
    private String ownerName;
    private String judgeName;

    public Competition(int id, int place, int dogId, int judgeId, String dogName, String dogBreed, String ownerName, String judgeName) {
        this.id = id;
        this.place = place;
        this.dogId = dogId;
        this.judgeId = judgeId;
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
    public int getDogId() {
        return dogId;
    }
    public int getJudgeId() {
        return judgeId;
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
    public void setDogId(int dogId) {
        this.dogId = dogId;
    }
    public void setJudgeId(int judgeId) {
        this.judgeId = judgeId;
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