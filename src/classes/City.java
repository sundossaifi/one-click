package classes;

public class City
{
    private int cityID;
    private String name;
    private String picPath;
    private String gifPath;
    private String description;

    public City(int cityID,String name,String picPath,String gifPath,String description)
    {
        this.setCityID(cityID);
        this.setName(name);
        this.setPicPath(picPath);
        this.setGifPath(gifPath);
        this.setDescription(description);
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGifPath() {
        return gifPath;
    }

    public void setGifPath(String gifPath) {
        this.gifPath = gifPath;
    }
}