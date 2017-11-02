package com.hikinghelper.cody.hikinghelper;

/**
 * Created by Cody on 11/1/2017.
 */

public class userData {

    private String name;
    private int age;
    private String experience;
    private String aboutMe;

    public void setName(String name){
        this.name = name;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setExperience(String experience){
        this.experience = experience;
    }

    public void setAboutMe(String aboutMe){
        this.aboutMe = aboutMe;
    }

    public String getName(){
        return this.name;
    }

    public int getAge(){
        return this.age;
    }

    public String getExperience(){
        return this.experience;
    }

    public String getAboutMe(){
        return this.aboutMe;
    }
}
