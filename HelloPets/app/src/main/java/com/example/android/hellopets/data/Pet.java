package com.example.android.hellopets.data;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by wkp on 2016/12/4.
 */
public class Pet implements Serializable {

    //Fields
    private Integer id;
    private Integer roomid;
    private String petname;
    private String breed;
    private Integer age;
    private String sex;
    private Date entertime;
    private Date leavetime;
    private Integer isback;
    private String backreason;
    private String character;
    private String healthy;
    private String img;
    private Integer istaken;

    // 方便进行申请的查看
    private Integer ispass;

    // Constructors

    /**
     * default constructor
     */
    public Pet() {

    }

    /**
     * full constructor
     */
    public Pet(Integer id, Integer roomid, String petname, String breed, Integer age, String sex, Date entertime, Date leavetime, Integer isback, String backreason, String character, String healthy, String img, Integer istaken) {
        this.id = id;
        this.roomid = roomid;
        this.petname = petname;
        this.breed = breed;
        this.age = age;
        this.sex = sex;
        this.entertime = entertime;
        this.leavetime = leavetime;
        this.isback = isback;
        this.backreason = backreason;
        this.character = character;
        this.healthy = healthy;
        this.img = img;
        this.istaken = istaken;
    }

    /**
     * useful constructor
     */
    public Pet(Integer id, String petname, String breed, Integer age, String sex, Date entertime, Integer isback, String backreason, String character, String healthy, String img) {
        this.id = id;
        this.petname = petname;
        this.breed = breed;
        this.age = age;
        this.sex = sex;
        this.entertime = entertime;
        this.isback = isback;
        this.backreason = backreason;
        this.character = character;
        this.healthy = healthy;
        this.img = img;
    }

    /**
     * used for look application
     */
    public Pet(Integer id, String petname, String breed, Integer age, String sex, Date entertime, Integer isback, String backreason, String character, String healthy, String img, Integer ispass) {
        this.id = id;
        this.petname = petname;
        this.breed = breed;
        this.age = age;
        this.sex = sex;
        this.entertime = entertime;
        this.isback = isback;
        this.backreason = backreason;
        this.character = character;
        this.healthy = healthy;
        this.img = img;
        this.ispass = ispass;
    }

    // Property accessors

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getEntertime() {
        return entertime;
    }

    public void setEntertime(Date entertime) {
        this.entertime = entertime;
    }

    public Date getLeavetime() {
        return leavetime;
    }

    public void setLeavetime(Date leavetime) {
        this.leavetime = leavetime;
    }

    public Integer getIsback() {
        return isback;
    }

    public void setIsback(Integer isback) {
        this.isback = isback;
    }

    public String getBackreason() {
        return backreason;
    }

    public void setBackreason(String backreason) {
        this.backreason = backreason;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getHealthy() {
        return healthy;
    }

    public void setHealthy(String healthy) {
        this.healthy = healthy;
    }

    public Integer getIspass() {
        return ispass;
    }

    public void setIspass(Integer ispass) {
        this.ispass = ispass;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getIstaken() {
        return istaken;
    }

    public void setIstaken(Integer istaken) {
        this.istaken = istaken;
    }
}
