package classes;

import java.util.ArrayList;

public class Car
{
    private int carID;
    private String carName;
    private String carType;
    private String carColor;
    private int cityID;
    private int price;
    private ArrayList<String> pictures;

    public Car(int carID,String carName,String carType,String carColor,int cityID,int price,ArrayList<String> pictures)
    {
        this.setCarID(carID);
        this.setCarName(carName);
        this.setCarType(carType);
        this.setCarColor(carColor);
        this.setCityID(cityID);
        this.setPrice(price);
        this.setPictures(pictures);
    }

    public int getCarID()
    {
        return carID;
    }

    public void setCarID(int carID)
    {
        this.carID = carID;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public int getCityID()
    {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}