package controllers;

import Interface.CarListener;
import classes.Car;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Caritemcontroller
{
    private Car car;
    private CarListener carListener;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView itemPicture;

    @FXML
    private Label nLabel;

    @FXML
    private Label pLabel;

    @FXML
    void changeCursor(Event event)
    {
        if(event.getSource()==this.anchorPane)
        {
            this.anchorPane.setCursor(Cursor.HAND);
        }
    }

    @FXML
    void click()
    {
        this.carListener.onClickListener(this.car);
    }

    public void setData(Car car, CarListener carListener)
    {
        this.car = car;
        this.carListener = carListener;
        this.nLabel.setText(car.getCarName()+"-"+car.getCarType());
        this.pLabel.setText(Integer.toString(car.getPrice()));
        this.itemPicture.setImage(new Image(new File(car.getPictures().get(0)).toURI().toString()));
    }
}