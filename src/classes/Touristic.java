package classes;

import java.util.ArrayList;

public class Touristic
{
    private int id;
    private String name;
    private String location;
    private String description;
    private int cid;
    private ArrayList<String> pictures;

    public Touristic(int id,String name,String location,String description,int cid,ArrayList<String> pictures)
    {
        this.setId(id);
        this.setName(name);
        this.setLocation(location);
        this.setDescription(description);
        this.setCid(cid);
        this.setPictures(pictures);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }
}