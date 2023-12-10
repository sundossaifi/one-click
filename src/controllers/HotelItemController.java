package controllers;


import Interface.HotelListener;
import classes.Hotel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class HotelItemController
{
    private Hotel hotel;
    private HotelListener hotelListener;
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
        this.hotelListener.onClickListener(this.hotel);
    }

    public void setData(Hotel hotel, HotelListener hotelListener)
    {
        this.hotel = hotel;
        this.hotelListener = hotelListener;
        this.nLabel.setText(hotel.getHotelName());
        this.pLabel.setText(Integer.toString(hotel.getRate()));
        this.itemPicture.setImage(new Image(new File(hotel.getPic()).toURI().toString()));
    }
}