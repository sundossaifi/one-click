package controllers;

import Interface.CityListener;
import classes.City;
import classes.Stuff;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class item1controller
{
    private City city;
    private CityListener cityListener;

    @FXML
    private Rectangle rectangle;
    @FXML
    private Label cityName;
    public void setData(City city,CityListener cityListener)
    {
        this.city=city;
        this.cityListener=cityListener;

        this.rectangle.setFill(new ImagePattern(new Image(new File(city.getPicPath()).toURI().toString())));
        this.cityName.setText(city.getName());
    }

    @FXML
    void onClick()
    {
        this.cityListener.onClickListener(this.city);
    }

    @FXML
    private void changeToGIF()
    {
        this.rectangle.setFill(new ImagePattern(new Image(new File(city.getGifPath()).toURI().toString())));
    }

    @FXML
    private void changeToDefault()
    {
        this.rectangle.setFill(new ImagePattern(new Image(new File(city.getPicPath()).toURI().toString())));
    }
}