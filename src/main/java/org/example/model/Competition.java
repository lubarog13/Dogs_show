package org.example.model;

/**
 * Класс, представляющий соревнование в системе.
 * Содержит информацию о результате соревнования, участниках (собака, владелец, судья) и занятом месте.
 */
public class Competition {
    private int id;
    private int place;
    private int dogId;
    private int judgeId;
    private int ownerId;
    private String dogName;
    private String dogBreed;
    private String ownerName;
    private String judgeName;

    /**
     * Конструктор для создания объекта Competition.
     *
     * @param id идентификатор соревнования
     * @param place занятое место в соревновании
     * @param dogId идентификатор собаки-участника
     * @param judgeId идентификатор судьи
     * @param ownerId идентификатор владельца собаки
     * @param dogName кличка собаки
     * @param dogBreed порода собаки
     * @param ownerName полное имя владельца
     * @param judgeName полное имя судьи
     */
    public Competition(int id, int place, int dogId, int judgeId, int ownerId, String dogName, String dogBreed, String ownerName, String judgeName) {
        this.id = id;
        this.place = place;
        this.dogId = dogId;
        this.judgeId = judgeId;
        this.ownerId = ownerId;
        this.dogName = dogName;
        this.dogBreed = dogBreed;
        this.ownerName = ownerName;
        this.judgeName = judgeName;
    }
    
    /**
     * Возвращает идентификатор соревнования.
     *
     * @return идентификатор
     */
    public int getId() {
        return id;
    }
    
    /**
     * Возвращает занятое место в соревновании.
     *
     * @return место
     */
    public int getPlace() {
        return place;
    }
    
    /**
     * Возвращает идентификатор собаки-участника.
     *
     * @return идентификатор собаки
     */
    public int getDogId() {
        return dogId;
    }
    
    /**
     * Возвращает кличку собаки-участника.
     *
     * @return кличка собаки
     */
    public String getDogName() {
        return dogName;
    }
    
    /**
     * Возвращает породу собаки-участника.
     *
     * @return порода собаки
     */
    public String getDogBreed() {
        return dogBreed;
    }
    
    /**
     * Возвращает полное имя владельца собаки.
     *
     * @return имя владельца
     */
    public String getOwnerName() {
        return ownerName;
    }
    
    /**
     * Возвращает полное имя судьи соревнования.
     *
     * @return имя судьи
     */
    public String getJudgeName() {
        return judgeName;
    }
    
    /**
     * Возвращает идентификатор владельца собаки.
     *
     * @return идентификатор владельца
     */
    public int getOwnerId() {
        return ownerId;
    }
    
    /**
     * Возвращает идентификатор судьи соревнования.
     *
     * @return идентификатор судьи
     */
    public int getJudgeId() {
        return judgeId;
    }
    
    /**
     * Устанавливает идентификатор соревнования.
     *
     * @param id идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Устанавливает занятое место в соревновании.
     *
     * @param place место
     */
    public void setPlace(int place) {
        this.place = place;
    }
    
    /**
     * Устанавливает идентификатор собаки-участника.
     *
     * @param dogId идентификатор собаки
     */
    public void setDogId(int dogId) {
        this.dogId = dogId;
    }
    
    /**
     * Устанавливает кличку собаки-участника.
     *
     * @param dogName кличка собаки
     */
    public void setDogName(String dogName) {
        this.dogName = dogName;
    }
    
    /**
     * Устанавливает породу собаки-участника.
     *
     * @param dogBreed порода собаки
     */
    public void setDogBreed(String dogBreed) {
        this.dogBreed = dogBreed;
    }
    
    /**
     * Устанавливает идентификатор владельца собаки.
     *
     * @param ownerId идентификатор владельца
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    
    /**
     * Устанавливает полное имя владельца собаки.
     *
     * @param ownerName имя владельца
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    /**
     * Устанавливает полное имя судьи соревнования.
     *
     * @param judgeName имя судьи
     */
    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }
    
    /**
     * Устанавливает идентификатор судьи соревнования.
     *
     * @param judgeId идентификатор судьи
     */
    public void setJudgeId(int judgeId) {
        this.judgeId = judgeId;
    }
}