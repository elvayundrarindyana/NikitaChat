package com.juaracoding.nikitachat.Model;

public class PeopleModel {
    public PeopleModel(){

    }
    public PeopleModel(String name, String image){
        this.name=name;
    this.image=image;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;
}
