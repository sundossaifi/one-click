package controllers;

import Interface.TouristicListener;
import classes.Touristic;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.File;

public class TouristicItemController
{
    private Touristic touristic;
    private TouristicListener touristicListener;
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
        this.touristicListener.onClickListener(this.touristic);
    }

    public void setData(Touristic touristic, TouristicListener touristicListener)
    {
        this.touristic = touristic;
        this.touristicListener = touristicListener;
        this.nLabel.setText(touristic.getName());
        this.pLabel.setText(touristic.getLocation());
        this.itemPicture.setImage(new Image(new File(touristic.getPictures().get(0)).toURI().toString()));
    }
}