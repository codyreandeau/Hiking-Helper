package com.hikinghelper.cody.hikinghelper;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Cody on 11/1/2017.
 */

public class userData extends AppCompatActivity {

    private String name;
    private String age;
    private String experience;
    private String aboutMe;

    public void setName(String name){
        this.name = name;
    }

    public void setAge(String age){
        this.age = age;
    }

    public void setExperience(String experience){
        this.experience = experience;
    }

    public void setAboutMe(String aboutMe){
        this.aboutMe = aboutMe;
    }

    public String getName(){
        return name;
    }

    public String getAge(){
        return age;
    }

    public String getExperience(){
        return experience;
    }

    public String getAboutMe(){
        return aboutMe;
    }
}
