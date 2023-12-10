package controllers;


import Interface.HotelListener;
import Interface.RestaurantListener;
import classes.Hotel;
import classes.Restaurant;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class RestaurantItemController
{
    private Restaurant restaurant;
    private RestaurantListener restaurantListener;
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
        this.restaurantListener.onClickListener(this.restaurant);
    }

    public void setData(Restaurant restaurant, RestaurantListener restaurantListener)
    {
        this.restaurant = restaurant;
        this.restaurantListener = restaurantListener;
        this.nLabel.setText(restaurant.getName());
        this.pLabel.setText(restaurant.getLocation());
        this.itemPicture.setImage(new Image(new File(restaurant.getPic()).toURI().toString()));
    }
}